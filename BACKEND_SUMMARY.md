# Backend Summary

## Overview
This file summarizes the backend work completed for the ecommerce application. It is designed to help frontend developers understand available APIs, validation rules, and expected response behavior.

## What was implemented

- Authentication
  - Register users with username/email/role
  - Login by username or email
  - Admin login returns a distinct success message
  - Passwords are stored using BCrypt hashing
  - Password policy enforced: minimum 8 chars, uppercase, lowercase, number, special symbol
  - Duplicate username and duplicate email checks with clear error messages

- API response standardization
  - All POST/PUT/DELETE endpoints return `ApiResponse` objects with a `message` field
  - Success responses often include `data`
  - Error responses return `message` directly via a global exception handler

- Product management
  - Bulk insert products
  - Read products with optional category filtering
  - Update and delete products with success messages

- Shopping cart
  - Add item to cart
  - View user cart
  - Update quantity
  - Remove item from cart

- Orders and payments
  - Place order from cart
  - Clear cart after order placement
  - Reduce product stock quantity when order is placed
  - Process payment for an order with `SUCCESS` status

- Invoice support
  - Generate invoice PDF and text file for orders

## Important backend behaviors for frontend

- All endpoints are under `http://localhost:9090/api`
- Authentication uses query parameters on login: `/api/users/login?username=...&password=...`
- Error responses are JSON objects with a `message` field only
- Successful responses for many operations include both `message` and `data`
- The `data` object for user registration and login includes the user record with hashed password in the database
- Product availability is enforced at order placement time

## Recommended frontend integration

- Use `ApiResponse.message` for user-facing success/error notifications
- Use `ApiResponse.data` when present for returned objects
- Validate password strength on the client before sending registration requests
- Show specific UI errors for:
  - weak password
  - duplicate username
  - duplicate email
  - insufficient stock
- For admin pages, ensure separate flows for product management vs customer shopping

## Why this is useful for Angular frontend later

Yes, this summary is very useful:
- it documents backend API behavior clearly
- it makes it easy to map frontend forms to backend endpoints
- it clarifies validation rules and error messages
- it helps avoid backend/frontend mismatches during integration

Use this file as a reference when building Angular components, services, and error handling.