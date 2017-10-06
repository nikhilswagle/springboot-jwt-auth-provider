This is a Spring Boot application that implements JWT authentication mechanism.
Following is the sequence of events in the process of authentication

Step 1.
User sends authentication request with accessId and secret

Step 2.
Server authenticates the user and sends back a JWT in the response, upon successful authentication.

Step 3.
User makes an api call with JWT in the Authorization header.

Step 4.
Server validates JWT and performs the API call upon successful validation.
If the token is invalid then user will recieve 401:UNAUTHORIZED response.