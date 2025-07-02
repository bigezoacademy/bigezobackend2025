# Bigezo Backend

This is the backend for Bigezo 2025, built with Spring Boot.

## Prerequisites

- Java 17
- Maven
- MySQL 8.0+
- Git

## Local Development

1. Clone the repository
2. Copy `.env.example` to `.env` and update the values
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`

## Deployment to Render.com

### Prerequisites
- A Render.com account
- GitHub/GitLab/Bitbucket repository with your code

### Steps

1. **Push your code to a Git repository**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin <your-repository-url>
   git push -u origin main
   ```

2. **Create a new Web Service on Render**
   - Go to [Render Dashboard](https://dashboard.render.com/)
   - Click "New" and select "Web Service"
   - Connect your Git repository
   - Configure the service:
     - Name: `bigezo-backend`
     - Region: Choose the one closest to your users
     - Branch: `main`
     - Runtime: `Java`
     - Build Command: `mvn clean package -DskipTests`
     - Start Command: `java -jar target/backend-0.0.1-SNAPSHOT.jar`

3. **Set up Environment Variables**
   - In the Render dashboard, go to your service
   - Click on "Environment"
   - Add all the environment variables from your `.env` file
   - Make sure to update the database connection string to use the Render database

4. **Set up a Database**
   - In the Render dashboard, click "New" and select "PostgreSQL"
   - Name it `bigezo-db`
   - Select the free plan
   - Connect it to your web service

5. **Deploy**
   - Click "Create Web Service"
   - Render will automatically build and deploy your application

## Environment Variables

See `.env.example` for a list of required environment variables.

## API Documentation

API documentation is available at `/swagger-ui.html` when running locally.

## License

This project is proprietary and confidential.
