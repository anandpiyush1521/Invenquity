# Invenquity

<p align="center">
  <img src="https://img.shields.io/badge/Powered_by-Spring_Boot-green?style=for-the-badge&logo=spring-boot" alt="Powered by Spring Boot">
  <img src="https://img.shields.io/badge/Powered_by-React-blue?style=for-the-badge&logo=react" alt="Powered by React">
  <img src="https://img.shields.io/badge/Powered_by-Tailwind_CSS-06B6D4?style=for-the-badge&logo=tailwind-css" alt="Powered by Tailwind CSS">
  <img src="https://img.shields.io/badge/Powered_by-PostgreSQL-316192?style=for-the-badge&logo=postgresql" alt="Powered by PostgreSQL">
</p>

## Overview

**Invenquity** is a high-quality inventory management system designed to streamline and optimize inventory processes. This project comprises two main components:

- **Frontend**: A user-friendly interface built with React and Tailwind CSS.
- **Backend**: A robust API developed using Java, Spring Boot, and PostgreSQL.

<p align="center">
  <img src="https://res.cloudinary.com/dth5ysuhs/image/upload/v1735325237/Screenshot_204_tvj1hf.png" width="1000" alt="Home Image" style="border: 2px solid beige;">
</p>

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Real-time Inventory Tracking**: Monitor stock levels in real-time.
- **User Authentication**: Secure login and registration system.
- **Responsive Design**: Accessible on various devices.
- **RESTful API**: Efficient communication between frontend and backend.
- **Session Management**: Users can extend their session durations for uninterrupted access.
- **Error Handling**: Comprehensive error handling mechanisms for smooth operations.

## Architecture

The project follows a client-server architecture:

- **Backend**:
  - **Language**: Java
  - **Framework**: Spring Boot
  - **Database**: PostgreSQL
  - **Repository**: [Invenquity Backend](https://github.com/anandpiyush1521/Invenquity)

- **Frontend**:
  - **Framework**: React
  - **Styling**: Tailwind CSS
  - **Repository**: [Invenquity Frontend](https://github.com/anandpiyush1521/Invenquity-frontend)

## Getting Started

### Prerequisites

- **Node.js** (v14 or above)
- **npm** (v6 or above)
- **Java** (JDK 11 or above)
- **Maven** (v3.6 or above)
- **PostgreSQL** (v12 or above)

### Installation

1. **Clone the repositories**:

   ```bash
   git clone https://github.com/anandpiyush1521/Invenquity-frontend.git
   git clone https://github.com/anandpiyush1521/Invenquity.git
   ```

2. **Set up the backend**:

   - Navigate to the backend directory:

     ```bash
     cd Invenquity
     ```

   - Configure the PostgreSQL database settings in `application.properties`.

   - Build the project using Maven:

     ```bash
     mvn clean install
     ```

   - Run the Spring Boot application:

     ```bash
     mvn spring-boot:run
     ```

3. **Set up the frontend**:

   - Navigate to the frontend directory:

     ```bash
     cd ../Invenquity-frontend
     ```

   - Install the dependencies:

     ```bash
     npm install
     ```

   - Start the development server:

     ```bash
     npm start
     ```

## Usage

- Access the frontend application at `http://localhost:3000`.
- The backend API is available at `http://localhost:8080`.

## Contributing

Contributions are welcome! Please fork the repositories and submit pull requests.

## License

This project is licensed under the MIT License.

---

For detailed information, please refer to the individual repositories:

- [Invenquity Frontend](https://github.com/anandpiyush1521/Invenquity-frontend)
- [Invenquity Backend](https://github.com/anandpiyush1521/Invenquity)

*Note: Ensure that all environment variables and configurations are properly set before running the applications.*
