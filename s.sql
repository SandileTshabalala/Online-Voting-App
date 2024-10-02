DESCRIBE voting_system.voters;
ALTER TABLE voting_system.voters DROP COLUMN isTokenUsed;
ALTER TABLE voting_system.voters ADD COLUMN isTokenUsed BOOLEAN DEFAULT FALSE;
SHOW COLUMNS FROM  voting_system.voters; 

ALTER TABLE voting_system.candidates 
ADD COLUMN electionId INT, 
ADD CONSTRAINT fk_electionId 
FOREIGN KEY (electionId) REFERENCES elections(id);


CREATE TABLE voting_system.voter_elections (
    voterId INT,
    electionId INT,
    PRIMARY KEY (voterId, electionId),
    FOREIGN KEY (voterId) REFERENCES voters(id),
    FOREIGN KEY (electionId) REFERENCES elections(id)
);

CREATE TABLE voting_system.votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    voterId INT NOT NULL,
    electionId INT NOT NULL,
    candidateId INT, -- Nullable, in case of abstentions
    timestamp VARCHAR(255),
    FOREIGN KEY (voterId) REFERENCES voters(id),
    FOREIGN KEY (electionId) REFERENCES elections(id),
    FOREIGN KEY (candidateId) REFERENCES candidates(id)
);

ALTER TABLE voting_system.candidates
ADD COLUMN supportingDocumentsUrl VARCHAR(255),
ADD COLUMN manifestoVideoUrl VARCHAR(255),
ADD COLUMN votesCount INT DEFAULT 0,
ADD COLUMN votePercentage DOUBLE DEFAULT 0.0;
ALTER TABLE voting_system.candidates MODIFY COLUMN address VARCHAR(255) NULL;
ALTER TABLE voting_system.candidates
ADD COLUMN profilePictureUrl VARCHAR(255);
ALTER TABLE voting_system.candidates MODIFY COLUMN dateOfBirth DATE NOT NULL;
ALTER TABLE voting_system.votes ADD abstain BOOLEAN DEFAULT FALSE NOT NULL;

-- start coding/creating database here
CREATE TABLE voting_system.elections (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    startDate DATE,
    endDate DATE,
    status VARCHAR(50),
    positions VARCHAR(255)
);
CREATE TABLE voting_system.candidates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullName VARCHAR(255),
    dateOfBirth DATE,
    email VARCHAR(255),
    phoneNumber VARCHAR(20),
    address VARCHAR(255),
    candidateId VARCHAR(255),
    partyAffiliation VARCHAR(255),
    position VARCHAR(255),
    profilePicture BLOB,
    slogan VARCHAR(255),
    biography TEXT,
    qualifications TEXT,
    manifestoTitle VARCHAR(255),
    manifestoText TEXT,
    endorsements TEXT,
    manifestoVideo BLOB,
    supportingDocuments BLOB,
    status VARCHAR(50),
    votesCount INT DEFAULT 0,
    votePercentage DOUBLE DEFAULT 0.0,
    profilePictureUrl VARCHAR(255),
    supportingDocumentsUrl VARCHAR(255),
    manifestoVideoUrl VARCHAR(255),
    electionId INT NOT NULL,
    FOREIGN KEY (electionId) REFERENCES elections(id)
);
CREATE TABLE voting_system.voters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    surName VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    studentNumber VARCHAR(255),
    phoneNumber VARCHAR(20),
    password VARCHAR(255),
    status VARCHAR(50),
    registrationDate TIMESTAMP,
    votingToken VARCHAR(255) UNIQUE,
    isTokenUsed BOOLEAN DEFAULT FALSE
);
CREATE TABLE voting_system.votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    abstain BOOLEAN NOT NULL,
    timestamp TIMESTAMP,
    votingToken VARCHAR(255) NOT NULL UNIQUE,
    electionId INT NOT NULL,
    candidateId INT,
    FOREIGN KEY (electionId) REFERENCES elections(id),
    FOREIGN KEY (candidateId) REFERENCES candidates(id)
);
CREATE TABLE voting_system.voter_elections (
    voterId INT,
    electionId INT,
    PRIMARY KEY (voterId, electionId),
    FOREIGN KEY (voterId) REFERENCES voters(id),
    FOREIGN KEY (electionId) REFERENCES elections(id)
);
DROP INDEX votingToken ON voting_system.votes;
ALTER TABLE voting_system.votes ADD position VARCHAR(255);
ALTER TABLE voting_system.votes 
ADD UNIQUE KEY unique_vote (votingToken, electionId, position);


