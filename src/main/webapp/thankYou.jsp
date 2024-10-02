<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thank You</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
        }
        .thank-you-container {
            text-align: center;
            padding: 50px 0;
            margin-top: 100px;
        }
        .thank-you-container h1 {
            font-size: 2.5rem;
            color: #007bff;
        }
        .thank-you-container p {
            font-size: 1.2rem;
            color: #666;
        }
        .checkmark {
            font-size: 4rem;
            color: #007bff;
            animation: checkmarkAnimation 1s ease-in-out forwards;
        }

        @keyframes checkmarkAnimation {
            0% {
                opacity: 0;
                transform: scale(0);
            }
            50% {
                opacity: 1;
                transform: scale(1.2);
            }
            100% {
                transform: scale(1);
            }
        }

        footer {
            margin-top: 50px;
            padding: 20px 0;
            background-color: #007bff;
            color: #fff;
            text-align: center;
        }
        footer a {
            color: #fff;
            text-decoration: none;
        }
        footer a:hover {
            text-decoration: underline;
        }

        .countdown {
            margin-top: 20px;
            font-size: 1.2rem;
            color: #007bff;
        }
    </style>
</head>
<body>
    <div class="container thank-you-container">
        <div class="card mx-auto" style="width: 50%;">
            <div class="card-body">
                <span class="checkmark">&#10004;</span>
                <h1>Thank you!</h1>
                <p>We've got your responses recorded.</p>
                <p class="countdown">You will be redirected in <span id="countdown">5</span> seconds...</p>
            </div>
        </div>
    </div>

    <footer>
        <p>Copyright 1996-2024 Pearson Education Inc. or its affiliate(s). All rights reserved.</p>
        <p>
            <a href="#">Terms</a> |
            <a href="#">Privacy</a> |
            <a href="#">Contact</a>
        </p>
    </footer>

    <script>
        // Countdown and redirection
        let countdownNumber = 5;
        const countdownElement = document.getElementById('countdown');

        const countdownInterval = setInterval(() => {
            countdownNumber--;
            countdownElement.textContent = countdownNumber;

            if (countdownNumber <= 0) {
                clearInterval(countdownInterval);
                window.location.href = "home.jsp"; // Redirect to home.jsp after countdown
            }
        }, 2000); // Decrement every 2 second
    </script>
</body>
</html>
