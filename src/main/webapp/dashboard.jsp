<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.voting.dao.CandidateDao" %>
<%@ page import="com.voting.dao.ElectionDao" %>
<%@ page import="com.voting.dao.ResultsDao" %>
<%@ page import="com.voting.implementation.CandidateDaoImpl" %>
<%@ page import="com.voting.implementation.ResultsDaoImpl" %>
<%@ page import="com.voting.implementation.ElectionDaoImpl" %>
<%@ page import="com.voting.models.Candidate" %>
<%@ page import="com.voting.models.Election" %>
<%@ page import="com.voting.models.Voter" %>
<%@ page import="com.voting.models.Results" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Dashboard</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card-box {
            border-radius: 10px;
            padding: 20px;
            color: #fff;
            margin-bottom: 30px;
        }
        .bg-purple { background-color: #7c4dff; }
        .bg-blue { background-color: #4fc3f7; }
        .bg-pink { background-color: #ff4081; }
        .bg-green { background-color: #66bb6a; }
        .stats-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    </style>
</head>
<body>
    <div class="container-fluid mt-5">
        <h2 class="text-center mb-4">Dashboard</h2>

        <!-- Stats row -->
        <div class="row">
            <div class="col-md-4">
                <div class="card-box bg-purple">
                    <h3>Elections</h3>
                    <p></p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card-box bg-pink">
                    <h3>Voters</h3>
                    <p></p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card-box bg-blue">
                    <h3>Candidates</h3>
                    <p></p>
                </div>
            </div>
        </div>

        <!-- Clients and Agents Stats -->
        <div class="row">
            <div class="col-md-6">
                <div class="card-box bg-blue">
                    <h3>Vote Count</h3>
                    <p></p>
                    <div class="stats-container">
                        <div>
                            <p>Active Elections</p>
                            <p></p>
                        </div>
                        <div>
                            <p>Inactive Elections</p>
                            <p></p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card-box bg-green">
                    <h3>Total Voters</h3>
                    <p></p>
                    <div class="stats-container">
                        <div>
                            <p>Approved Voters</p>
                            <p></p>
                        </div>
                        <div>
                            <p>Rejected Voters</p>
                            <p></p>
                        </div>
                        <div>
                            <p>Suspended Voters</p>
                            <p></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Include Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
