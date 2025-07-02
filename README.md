# Online Voting Application

A web-based voting system that enables secure and efficient online elections management.

## Features

- **User Roles**:
  - Voters: Register and cast votes after admin approval
  - Candidates: Register for elections and await admin verification
  - Admin: Manage voter and candidate approvals, send notifications

- **Security Features**:
  - User authentication and authorization
  - Email verification system
  - Vote validation and counting
  - Admin-only approval system

## Project Structure

The project is organized into several key components:

- `src/`: Source code directory
- `config/`: Configuration files
- `resources/`: Static resources and assets
- `web/`: Web application files

## System Requirements

- Java Development Kit (JDK) 8 or higher
- Apache NetBeans IDE
- MySQL Database
- Java EE compatible web server (e.g., Apache Tomcat)

## Setup Instructions

### Using NetBeans IDE

1. **Clone the Repository**:
   - Open NetBeans IDE
   - Click on `Team` in the top menu
   - Select `Git` -> `Clone`
   - Enter the repository URL (GitHub URL)
   - Choose the local directory where you want to clone the project
   - Click `Next` and then `Finish`

2. **Import the Project**:
   - After cloning, the project should automatically appear in NetBeans
   - If not, go to `File` -> `Open Project`
   - Navigate to the cloned project directory
   - Select the project folder and click `Open Project`

3. **Configure Database**:
   - Open `config/database.properties`
   - Update database connection settings:
     - Database URL
     - Username
     - Password

4. **Deploy the Application**:
   - Right-click on the project in the Projects window
   - Select `Run` or press `F6`
   - The application will be deployed to your configured server

5. **Access the Application**:
   - Open your web browser
   - Navigate to `http://localhost:8080/OnlineVotingApp` (or your configured port)
   - The application should now be accessible

## Usage

1. **Admin Setup**:
   - Log in with admin credentials
   - Configure election parameters
   - Manage voter and candidate registrations

2. **Voter Registration**:
   - Complete registration form
   - Wait for admin approval
   - Receive email notification upon approval

3. **Candidate Registration**:
   - Submit candidate application
   - Wait for admin verification
   - Receive email notification upon verification

## Security Considerations

- All user data is protected
- Votes are securely counted and stored
- Admin access is restricted
- Email notifications are encrypted

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, please open an issue in the GitHub repository.

## Acknowledgments

- Thanks to all contributors and users
- Special thanks to the Apache NetBeans team
