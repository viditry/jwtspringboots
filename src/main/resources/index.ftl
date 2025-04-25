<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>ElectroDrive Login</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Font Awesome for icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <style>
    :root {
      --primary: #00a8e8;
      --secondary: #007ea7;
      --accent: #59ce8f;
      --light: #f8f9fa;
      --dark: #003459;
    }

    body {
      background-color: var(--light);
      height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    .login-container {
      max-width: 900px;
      width: 100%;
      background-color: white;
      border-radius: 20px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
      overflow: hidden;
    }

    .car-container {
      position: relative;
      height: 200px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 20px 0;
    }

    .car {
      position: relative;
      width: 300px;
      height: 120px;
    }

    .car-body {
      position: absolute;
      width: 300px;
      height: 60px;
      background: var(--primary);
      bottom: 24px;
      border-radius: 35px 80px 20px 20px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .car-top {
      position: absolute;
      width: 150px;
      height: 60px;
      background: var(--primary);
      top: 0;
      left: 60px;
      border-radius: 40px 70px 0 0;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .window {
      position: absolute;
      width: 120px;
      height: 40px;
      background: #e0f7fa;
      top: 10px;
      left: 75px;
      border-radius: 30px 60px 0 0;
    }

    .wheel {
      position: absolute;
      width: 40px;
      height: 40px;
      background: #333;
      border-radius: 50%;
      bottom: 0;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .wheel-left {
      left: 50px;
    }

    .wheel-right {
      right: 50px;
    }

    .wheel-cover {
      position: absolute;
      width: 24px;
      height: 24px;
      background: #777;
      border-radius: 50%;
      top: 8px;
      left: 8px;
    }

    .headlight {
      position: absolute;
      width: 15px;
      height: 10px;
      background: #ffeb3b;
      border-radius: 5px;
      bottom: 50px;
      right: 10px;
    }

    .taillight {
      position: absolute;
      width: 10px;
      height: 8px;
      background: #f44336;
      border-radius: 3px;
      bottom: 50px;
      left: 15px;
    }

    .charging-port {
      position: absolute;
      width: 15px;
      height: 15px;
      background: #ccc;
      border-radius: 50%;
      top: 40px;
      right: 70px;
      border: 2px solid #999;
    }

    .charging-cable {
      position: absolute;
      width: 40px;
      height: 5px;
      background: var(--accent);
      top: 45px;
      right: 85px;
      border-radius: 5px;
    }

    .charging-station {
      position: absolute;
      width: 20px;
      height: 40px;
      background: #555;
      top: 27px;
      right: 125px;
      border-radius: 5px;
    }

    .road {
      position: absolute;
      width: 100%;
      height: 10px;
      background: #555;
      bottom: 0;
      border-radius: 5px;
    }

    .road-line {
      position: absolute;
      width: 30px;
      height: 3px;
      background: white;
      bottom: 3.5px;
    }

    .road-line:nth-child(1) { left: 10%; }
    .road-line:nth-child(2) { left: 25%; }
    .road-line:nth-child(3) { left: 40%; }
    .road-line:nth-child(4) { left: 55%; }
    .road-line:nth-child(5) { left: 70%; }
    .road-line:nth-child(6) { left: 85%; }

    .login-form {
      padding: 30px;
    }

    .form-control:focus {
      border-color: var(--primary);
      box-shadow: 0 0 0 0.25rem rgba(0, 168, 232, 0.25);
    }

    .btn-electric {
      background-color: var(--primary);
      border-color: var(--primary);
      color: white;
      padding: 10px 20px;
      font-weight: 600;
      border-radius: 30px;
      transition: all 0.3s ease;
    }

    .btn-electric:hover, .btn-electric:focus {
      background-color: var(--secondary);
      border-color: var(--secondary);
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .form-check-input:checked {
      background-color: var(--primary);
      border-color: var(--primary);
    }

    .electric-text {
      color: var(--primary);
    }

    .form-label {
      font-weight: 500;
      color: var(--dark);
    }

    .battery-indicator {
      position: absolute;
      width: 50px;
      height: 20px;
      background: #eee;
      border: 2px solid #ccc;
      border-radius: 3px;
      top: 20px;
      left: 50%;
      transform: translateX(-50%);
      overflow: hidden;
    }

    .battery-level {
      height: 100%;
      width: 80%;
      background: var(--accent);
    }

    .battery-cap {
      position: absolute;
      width: 5px;
      height: 10px;
      background: #ccc;
      right: -5px;
      top: 5px;
      border-radius: 0 3px 3px 0;
    }

    @keyframes pulse {
      0% { opacity: 0.7; }
      50% { opacity: 1; }
      100% { opacity: 0.7; }
    }

    .headlight {
      animation: pulse 2s infinite;
    }

    .input-group-text {
      background-color: var(--primary);
      color: white;
      border-color: var(--primary);
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-10 col-lg-8">
        <div class="login-container">
          <div class="text-center pt-4">
            <h2 class="fw-bold">ElectroDrive <span class="electric-text">Login</span></h2>
            <p class="text-muted">Power up your journey</p>
          </div>

          <div class="car-container">
            <div class="car">
              <div class="car-body"></div>
              <div class="car-top"></div>
              <div class="window"></div>
              <div class="wheel wheel-left">
                <div class="wheel-cover"></div>
              </div>
              <div class="wheel wheel-right">
                <div class="wheel-cover"></div>
              </div>
              <div class="headlight"></div>
              <div class="taillight"></div>
              <div class="charging-port"></div>
              <div class="charging-cable"></div>
              <div class="charging-station"></div>
              <div class="battery-indicator">
                <div class="battery-level"></div>
                <div class="battery-cap"></div>
              </div>
            </div>
            <div class="road">
              <div class="road-line"></div>
              <div class="road-line"></div>
              <div class="road-line"></div>
              <div class="road-line"></div>
              <div class="road-line"></div>
              <div class="road-line"></div>
            </div>
          </div>

          <div class="row justify-content-center">
            <div class="col-md-8">
              <div class="login-form">
                <form>
                  <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <div class="input-group">
                      <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                      <input type="email" class="form-control" id="email" placeholder="Enter your email">
                    </div>
                  </div>
                  <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <div class="input-group">
                      <span class="input-group-text"><i class="fas fa-lock"></i></span>
                      <input type="password" class="form-control" id="password" placeholder="Enter your password">
                    </div>
                  </div>
                  <div class="mb-3 d-flex justify-content-between align-items-center">
                    <div class="form-check">
                      <input type="checkbox" class="form-check-input" id="rememberMe">
                      <label class="form-check-label" for="rememberMe">Remember me</label>
                    </div>
                    <a href="#" class="text-decoration-none electric-text">Forgot password?</a>
                  </div>
                  <div class="d-grid gap-2">
                    <button type
                  </div>
                </form>
                <div class="text-center mt-4">
                  <p class="mb-0">Don't have an account? <a href="#" class="text-decoration-none electric-text">Sign up</a></p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Bootstrap JS Bundle with Popper -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>