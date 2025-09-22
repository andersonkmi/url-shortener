## URL Shortener 🚀
A lightweight and efficient URL Shortener toy project built on maven using Java and spring Boot is a tool to turn the cumbersome URLs into shorter, shareable links.

## Demo 🎬
 **Example:** 
   Shortening https://example.com/very/long/url → http://localhost:8080/abc123

## Features ✨

- Generate unique short URLs for any long URL.
- Redirect short URLs to their original long URLs.
- Track usage count for each short URL.
- Easy to extend and customize.

## Technologies 🛠️

- Java 21
- Spring Boot
- Gradle
- PostgreSQL
- JDBC
- Docker

## Database Setup 🗄️

This project uses PostgreSQL. Follow these steps to set it up:

- Install PostgreSQL if not already installed: https://www.postgresql.org/download/
- Create a database (<database_name>):
    ```
     CREATE DATABASE urlshortener;
    ```
- Update application.properties with your credentials:
   ```
      spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:urlshortner}
      spring.datasource.username=your_username
      spring.datasource.password=your_password
  ```
- Ensure PostgreSQL is running on localhost:5432 before starting the application.

For local development purposes, it is possible to setup a Docker container:

- Run the following docker command to create the database image:
  ```
     docker image build -t codecraftlabs/url-shortner-db:1.0.0 ./url-shortner-db/docker
  ```

- Create the container with the previously created image:
  ```
     docker container run --detach --name url-shortner-db --publish 5432:5432 codecraftlabs/url-shortner-db:1.0.0
  ```  

## Installation 💻

- Clone the repository:
   ```
      git clone <repository-url>
      cd url-shortener
   ```
- Build the project with Gradle:
  ```
     ./gradlew build      # On Linux/macOS
      gradlew.bat build    # On Windows
  ```
- Run the application:
   ```
      ./gradlew bootRun    # On Linux/macOS
       gradlew.bat bootRun  # On Windows
   ```
- Server will start at http://localhost:27001.

## API Endpoints 🧭

| Endpoint	                             | Method	 | Description                                                    |
|---------------------------------------|---------|----------------------------------------------------------------|
| /url-shortner/v1/url	                 | POST	   | Create a shortened URL from a long URL (send JSON in the body) |
| /url-shortner/v1/url/{shortenedUrl} 	 | GET	    | Redirect to the original URL using the short URL               |

## Contributing 🤝

Contributions welcome! Steps:
- Fork the repo.
- Create a new branch: git checkout -b feature/your-feature
- Make changes and commit: git commit -m "Add feature"
- Push branch: git push origin feature/your-feature
- Open a Pull Request.
  Even small contributions, such as fixing typos or improving formatting, are welcome.

## License 📄

This project is licensed under the MIT License. See the [LICENSE](https://github.com/andersonkmi/url-shortener/blob/main/LICENSE) file for details.

## Credits / Acknowledgements 🙏

This project was originally created by [Anderson Ito](https://github.com/andersonkmi). I have contributed by updating the README and other improvements.
