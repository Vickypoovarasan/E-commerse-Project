package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/{orderId}")
    public ResponseEntity<ByteArrayResource> generateInvoice(@PathVariable Long orderId) throws IOException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float margin = 50;
                float y = 760;
                float lineHeight = 18;

                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 22);
                content.newLineAtOffset(margin, y);
                content.showText("Invoice");
                content.endText();

                y -= 30;
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(margin, y);
                content.showText("Order ID: " + order.getOrderId());
                content.endText();

                y -= lineHeight;
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(margin, y);
                content.showText("Order Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                content.endText();

                y -= lineHeight;
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(margin, y);
                content.showText("Total Amount: ₹" + String.format("%.2f", order.getTotalAmount()));
                content.endText();

                y -= lineHeight;
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(margin, y);
                content.showText("Shipping Address: " + (order.getShippingAddress() == null ? "N/A" : order.getShippingAddress()));
                content.endText();

                y -= lineHeight;
                if (order.getDeliveryNote() != null && !order.getDeliveryNote().isEmpty()) {
                    content.beginText();
                    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    content.newLineAtOffset(margin, y);
                    content.showText("Delivery Note: " + order.getDeliveryNote());
                    content.endText();
                    y -= lineHeight;
                }

                y -= lineHeight;
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                content.newLineAtOffset(margin, y);
                content.showText("Items Purchased:");
                content.endText();

                y -= 22;
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                for (OrderItem item : items) {
                    content.beginText();
                    content.newLineAtOffset(margin, y);
                    content.showText(item.getProductName() + " x" + item.getQuantity() + " @ ₹" + String.format("%.2f", item.getUnitPrice()) + " = ₹" + String.format("%.2f", item.getTotalPrice()));
                    content.endText();
                    y -= lineHeight;
                }

                y -= lineHeight;
                content.beginText();
                content.newLineAtOffset(margin, y);
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                content.showText("Thank you for shopping with us!");
                content.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            byte[] bytes = outputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(bytes);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + orderId + ".pdf")
                    .body(resource);
        }
    }

    @GetMapping("/{orderId}/txt")
    public ResponseEntity<ByteArrayResource> generateInvoiceText(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        String invoiceText = createInvoiceText(order, items);
        byte[] bytes = invoiceText.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + orderId + ".txt")
                .body(resource);
    }

    private String createInvoiceText(Order order, List<OrderItem> items) {
        StringBuilder builder = new StringBuilder();
        builder.append("Invoice").append(System.lineSeparator());
        builder.append("Order ID: ").append(order.getOrderId()).append(System.lineSeparator());
        builder.append("Order Date: ").append(order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append(System.lineSeparator());
        builder.append("Total Amount: ₹").append(String.format("%.2f", order.getTotalAmount())).append(System.lineSeparator());
        builder.append("Shipping Address: ").append(order.getShippingAddress() == null ? "N/A" : order.getShippingAddress()).append(System.lineSeparator());

        if (order.getDeliveryNote() != null && !order.getDeliveryNote().isEmpty()) {
            builder.append("Delivery Note: ").append(order.getDeliveryNote()).append(System.lineSeparator());
        }

        builder.append(System.lineSeparator()).append("Items Purchased:").append(System.lineSeparator());
        for (OrderItem item : items) {
            builder.append("- ")
                    .append(item.getProductName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" @ ₹")
                    .append(String.format("%.2f", item.getUnitPrice()))
                    .append(" = ₹")
                    .append(String.format("%.2f", item.getTotalPrice()))
                    .append(System.lineSeparator());
        }

        builder.append(System.lineSeparator()).append("Thank you for shopping with us!").append(System.lineSeparator());
        return builder.toString();
    }
}
