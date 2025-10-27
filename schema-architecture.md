#Section 1 Architecture summary

This Spring Boot application combines MVC and REST architectures to handle different user interactions. Thymeleaf templates are employed for rendering the Admin and Doctor dashboards, providing server-side dynamic HTML views, while all other modules communicate via REST APIs that return JSON responses. The backend interacts with two distinct databases: MySQL, which stores structured data such as patients, doctors, appointments, and admin records, and MongoDB, which manages flexible prescription documents. All incoming requests pass through a centralized service layer that encapsulates business logic and delegates data operations to the corresponding repositories. Data retrieved from MySQL is mapped to JPA entities, whereas MongoDB documents are mapped to document models, ensuring consistent object-oriented handling across the application layers.

#Section 2: Numbered flow of data and control
1.User Interaction
Users access the system through either Thymeleaf-based dashboards (Admin and Doctor) or REST API clients (mobile apps or other modules). Their actions, such as submitting forms or requesting data, initiate a request to the backend.

2.Controller Handling
Incoming requests are routed to the appropriate controller. Thymeleaf Controllers render server-side HTML templates for dashboards, while REST Controllers process API calls and return JSON responses. Controllers validate inputs and coordinate the flow of each request.

3.Service Layer Processing
Controllers delegate all business logic to the Service Layer. This layer applies rules, validates data, and coordinates workflows, such as checking doctor availability before scheduling appointments, keeping business logic separate from controllers.

4.Repository Access
The Service Layer interacts with repositories to perform data operations. MySQL repositories handle structured relational data, while MongoDB repositories manage flexible, document-based records like prescriptions.

5.Database Operations
Repositories communicate directly with the underlying databases. MySQL enforces relational constraints for core entities, while MongoDB allows rapid schema evolution for documents with varying structures.

6.Model Binding
Data retrieved from databases is converted into Java objects. MySQL data is mapped to JPA entities, and MongoDB documents are mapped to document models, creating a consistent object-oriented representation for the application.

7.Response Rendering
Finally, models are passed back to the controllers for output. In MVC flows, data populates Thymeleaf templates for dynamic HTML pages. In REST flows, models or DTOs are serialized into JSON and sent to the client, completing the request-response cycle.
