<%@page import="java.util.List"%>
<%@page import="com.voting.models.Election"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Candidate Registration Form</title>
        
        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">      
          <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            background-image:url("https://cdn.ghanaguardian.com/2024/07/AI-elections_illo_SamanthaWongAdobeStock-750x375-1-610x375.webp");

            }
            .container {
                max-width: 800px;
                margin: 0 auto;
                padding: 20px;
                border: 1px solid #ddd;
                background-color: #fff;
                border-radius: 5px;
            }
            h2 {
                text-align: center;
                color:#28A745;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                display: block;
                margin-bottom: 5px;
            }
            .form-group input, .form-group textarea, .form-group select {
                width: 100%;
                padding: 8px;
                box-sizing: border-box;
            }
            .form-group button {
                display: block;
                width: 100%;
                padding: 10px;
                background-color: #28A745;
                color: white;
                border: none;
                border-radius: 5px;
                font-size: 16px;
                
            }
            .form-group button:hover {
                background-color: #0056b3;
            }
        </style>      
    </head>
    <body>
        <div class="container">
            <h2>Candidate Registration Form</h2>
            <form action="CandidateServlet" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="fullName">Full Name:</label>
                    <input type="text" id="fullName" name="fullName" required>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth">Date of Birth:</label>
                    <input type="date" id="dateOfBirth" name="dateOfBirth" required>
                </div>
                <div class="form-group">
                    <label for="email">Email Address:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                    <label for="phoneNumber">Phone Number:</label>
                    <input type="tel" id="phoneNumber" name="phoneNumber">
                </div>
                <div class="form-group">
                    <label for="address">Residential Address:</label>
                    <input type="text" id="address" name="address" required>
                </div>
                <div class="form-group">
                    <label for="partyAffiliation">Political Party Affiliation:</label>
                    <input type="text" id="partyAffiliation" name="partyAffiliation">
                </div>
                <div class="form-group">
                    <label for="election">Select Election:</label>
                    <select id="election" name="electionId" required onchange="fetchPositions(this.value)">
                        <option value="">-- Select Election --</option>
                        <%
                            List<Election> elections = (List<Election>) request.getAttribute("elections");
                            if (elections != null && !elections.isEmpty()) {
                                for (Election election : elections) {
                        %>
                        <option value="<%= election.getId()%>"><%= election.getName()%></option>
                        <%
                                }
                            } else {
                        %>
                        <option value="">No elections available</option>
                        <%
                            }
                        %>
                    </select>
                </div>
                <div class="form-group" id="position-group">
                    <label for="position">Select Position:</label>
                    <select id="position" name="position" required>
                        <option value="">-- Select Position --</option>
                    </select>
                    <p id="no-positions" style="display: none;">No positions available to apply for this election.</p>
                </div>
                <div class="form-group">
                    <label for="profilePicture">Profile Picture:</label>
                    <input type="file" id="profilePicture" name="profilePicture">
                </div>
                <div class="form-group">
                    <label for="slogan">Campaign Slogan or Statement:</label>
                    <input type="text" id="slogan" name="slogan">
                </div>
                <div class="form-group">
                    <label for="biography">Biography or Background:</label>
                    <textarea id="biography" name="biography" rows="4"></textarea>
                </div>
                <div class="form-group">
                    <label for="qualifications">Qualifications:</label>
                    <textarea id="qualifications" name="qualifications" rows="4"></textarea>
                </div>
                <div class="form-group">
                    <label for="manifestoTitle">Manifesto Title:</label>
                    <input type="text" id="manifestoTitle" name="manifestoTitle">
                </div>
                <div class="form-group">
                    <label for="manifestoText">Manifesto Text:</label>
                    <textarea id="manifestoText" name="manifestoText" rows="6"></textarea>
                </div>
                <div class="form-group">
                    <label for="supportingDocuments">Supporting Documents:</label>
                    <input type="file" id="supportingDocuments" name="supportingDocuments" multiple>
                    <small>Upload PDFs, images, or other documents that support your manifesto. You can upload multiple files.</small>
                </div>
                <div class="form-group">
                    <label for="endorsements">Public Endorsements:</label>
                    <textarea id="endorsements" name="endorsements" rows="4"></textarea>
                </div>
                <div class="form-group">
                    <label for="manifestoVideo">Manifesto Video (Optional):</label>
                    <input type="file" id="manifestoVideo" name="manifestoVideo" accept="video/*">
                </div>
                <div class="form-group">
                    <input type="hidden" id="candidateId" name="candidateId">
                </div>

                <div class="form-group">
                    <button type="submit">Register</button>
                </div>
            </form>
        </div>
                    
        <script>
            function fetchPositions(electionId) {
                const positionSelect = document.getElementById('position');
                positionSelect.innerHTML = '<option value="">-- Select Position --</option>';
                document.getElementById('no-positions').style.display = 'none';

                if (electionId) {
                    fetch('GetPositionsServlet?electionId=' + electionId)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok: ' + response.statusText);
                            }
                            return response.json();  // Parse JSON response
                        })
                        .then(data => {
                            const positions = data.positions;  // Access positions from the response

                            if (positions && positions.length > 0) {
                                // Populate positions dropdown
                                positions.forEach(function(position) {
                                    const option = document.createElement('option');
                                    option.value = position;  // Set the value to position name
                                    option.text = position;   // Set display text for the option
                                    positionSelect.appendChild(option);
                                });
                            } else {
                                // No positions available for this election
                                document.getElementById('no-positions').style.display = 'block';
                            }
                        })
                        .catch(error => console.error('Error fetching positions:', error));
                }
            }

            function refreshElections() {
                fetch('GetAllElectionsServlet')
                    .then(response => response.json())  // Ensure JSON response
                    .then(data => {
                        const electionSelect = document.getElementById('election');
                        electionSelect.innerHTML = '<option value="">-- Select Election --</option>';

                        data.forEach(election => {
                            const option = document.createElement('option');
                            option.value = election.id;
                            option.text = election.name;
                            electionSelect.appendChild(option);
                        });
                    })
                    .catch(error => console.error('Error fetching elections:', error));
            }

            function generateCandidateId() {
                // Generate a random 5-digit number
                const randomId = Math.floor(10000 + Math.random() * 90000);
                document.getElementById('candidateId').value = randomId;
                console.log('Generated Candidate ID:', randomId);
            }

            window.onload = function () {
                refreshElections();
                // Set a random candidateId when the form is submitted
                const form = document.querySelector('form');
                form.onsubmit = generateCandidateId;
            };
        </script>
    </body>
</html>
