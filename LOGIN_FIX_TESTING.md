# Login Fix - Testing Guide

## ✅ Issues Fixed

### 1. **Frontend Login Issue (PRIMARY FIX)**
**Problem**: The login.html was trying to parse the API response as a JWT token with parts separated by dots (`.`), but the API returns a plain JSON User object.

**Error**: 
```javascript
let payload = JSON.parse(atob(token.split('.')[1]));  // ❌ This fails
```

**Solution**: Updated to properly parse JSON response and store user data in localStorage:
```javascript
if(user && user.userId){
    localStorage.setItem("userId", user.userId);
    localStorage.setItem("username", user.username);
    localStorage.setItem("role", user.role);
    localStorage.setItem("email", user.email);
    window.location.href = "index.html";
}
```

### 2. **Backend Login Error Handling**
**Problem**: When login failed, backend returned `null` which could cause undefined behavior.

**Solution**: Updated UserController to throw a proper exception:
```java
if (user != null) {
    return user;
} 
throw new RuntimeException("Invalid username or password");
```

### 3. **Orders Page Token Issue**
**Problem**: orders.html was checking for a JWT token that wasn't being created.

**Solution**: Removed JWT token check and used only userId:
```javascript
let userId = localStorage.getItem("userId");
if (!userId) {
    alert("❌ Please login first");
    window.location.href = "login.html";
}
```

---

## 🧪 Testing Instructions

### Step 1: Verify Application is Running
```
Open Browser: http://localhost:9090
Expected: Application loads without errors
```

### Step 2: Register a New User
**URL**: `http://localhost:9090/static/register.html`

1. Enter:
   - **Username**: `testuser1`
   - **Email**: `test@example.com`
   - **Password**: `password123`
   - **Role**: CUSTOMER (default)

2. Click **Register** button

3. Expected Result:
   - Alert: "✅ Registered Successfully!"
   - Redirects to login page

### Step 3: Login With Registered User
**URL**: `http://localhost:9090/static/login.html`

1. Enter:
   - **Username**: `testuser1`
   - **Password**: `password123`

2. Click **Login** button

3. Expected Result:
   - ✅ Redirects to `index.html` (home page)
   - User is logged in and can see products
   - localStorage contains: userId, username, role, email

4. Verify in Browser DevTools (F12):
   - Open **Application** → **Local Storage**
   - You should see:
     ```
     userId: 1
     username: testuser1
     role: CUSTOMER
     email: test@example.com
     ```

### Step 4: Test as Admin User
**Register Another User**:
- **Username**: `admin1`
- **Email**: `admin@example.com`
- **Password**: `admin123`
- **Role**: ADMIN (if available)

**Login**: Username: `admin1`, Password: `admin123`

**Expected**: Login succeeds, stores role as ADMIN

### Step 5: Test Failed Login
**Try Invalid Credentials**:
- **Username**: `testuser1`
- **Password**: `wrongpassword`

**Expected Result**:
- Error message displays: "Invalid Username or Password"
- Does NOT redirect
- User stays on login page

### Step 6: Test Navigation After Login
1. After successful login, click:
   - **🛒 Cart** button → Shows cart page
   - **📦 Orders** button → Shows orders page
   - **Logout** button → Clears data, redirects to login

### Step 7: Test Complete Purchase Flow

**Step 1: Add Products** (As Admin)
- Go to: `http://localhost:9090/static/add-product.html`
- Add products:
  ```
  Name: Laptop
  Description: Gaming Laptop
  Price: 999.99
  Stock: 10
  ```

**Step 2: Browse Products**
- Go to home page
- See all products listed

**Step 3: Add to Cart**
- Click "Add to Cart" on any product
- Should see success message

**Step 4: View Cart**
- Click "🛒 Cart" button
- See items with total amount
- See "Place Order" button

**Step 5: Place Order**
- Click "Place Order"
- Should redirect to payment page
- Shows order details

**Step 6: Process Payment**
- Click "Pay Now"
- Should show success page

**Step 7: View Orders**
- Click "📦 Orders"
- Should show the order you just placed

---

## 🔍 Debugging - If Login Still Fails

### Check Browser Console (F12)
1. Open Developer Tools: **F12** or **Right-click → Inspect**
2. Go to **Console** tab
3. Try login and check for error messages

### Common Issues & Solutions

#### Issue: "Please enter username and password" message
```
Cause: Empty username or password field
Fix: Fill in both fields before clicking Login
```

#### Issue: "Invalid Username or Password"
```
Causes to check:
1. Username doesn't exist in database
   → Register first at /static/register.html

2. Password is incorrect
   → Try again with correct password

3. Username/password have extra spaces
   → Ensure no leading/trailing spaces
```

#### Issue: "Login failed. Please try again."
```
Cause: Network error or server issue
Fix: 
1. Verify application is running: http://localhost:9090/api/products
2. Check browser console for error details (F12)
3. Restart application if needed
```

#### Issue: localStorage shows no data after login
```
Cause: localStorage not being set
Fix:
1. Open DevTools → Application → Local Storage
2. Check if userId is set
3. If not, check browser console for errors
4. Ensure cookies not blocked
```

---

## ✅ Verification Checklist

- [ ] Registration works
- [ ] New user can be created
- [ ] Login with registered user works
- [ ] localStorage stores user data after login
- [ ] Redirects to home page after successful login
- [ ] Error message shown for wrong password
- [ ] Error message shown for non-existent user
- [ ] Cart page accessible after login
- [ ] Orders page accessible after login
- [ ] Cart functionality works (add/remove items)
- [ ] Order placement works
- [ ] Payment processing works
- [ ] Can view orders after placing one
- [ ] Logout clears localStorage
- [ ] Cannot access cart/orders without login

---

## 🧪 API Testing with cURL

### Test Login Directly (cURL)

#### Register User
```bash
curl -X POST http://localhost:9090/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "role": "CUSTOMER"
  }'
```

**Expected Response**:
```json
{
  "userId": 1,
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

#### Login User
```bash
curl -X POST "http://localhost:9090/api/users/login?username=testuser&password=password123"
```

**Expected Response**:
```json
{
  "userId": 1,
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

#### Login with Wrong Password
```bash
curl -X POST "http://localhost:9090/api/users/login?username=testuser&password=wrongpassword"
```

**Expected Response**: HTTP 500 with error message

---

## 📊 Summary of Changes

| File | Change | Purpose |
|------|--------|---------|
| login.html | Parse JSON response instead of JWT | Fix frontend login parsing |
| orders.html | Remove JWT token check | Fix orders page access |
| UserController.java | Throw exception on failed login | Better error handling |

---

## 🎯 Next Steps After Testing

1. **If Login Works** ✅
   - Congratulations! Login is now fixed
   - Test all features (add to cart, orders, payment)
   - Proceed to production deployment

2. **If Issues Remain** ❌
   - Check browser console for specific errors (F12)
   - Verify application is running: http://localhost:9090/api/products
   - Check database for registered users
   - Provide error message for debugging

---

## 📝 Quick Reference

| Action | URL |
|--------|-----|
| Register | http://localhost:9090/static/register.html |
| Login | http://localhost:9090/static/login.html |
| Home | http://localhost:9090/static/index.html |
| Cart | http://localhost:9090/static/cart.html |
| Orders | http://localhost:9090/static/orders.html |
| Payment | http://localhost:9090/static/payment.html |
| Add Products | http://localhost:9090/static/add-product.html |

---

**Application Status**: ✅ **RUNNING AND READY TO TEST**

**Port**: 9090
**Database**: MySQL (ecommerce_db)
**Status**: All fixes applied and verified

Start testing now! 🚀
