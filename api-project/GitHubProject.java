package projects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GitHubProject {
	// Declare request specification
	RequestSpecification requestSpec;
	// Global properties
	String sshKey;
	int sshKeyId;

	@BeforeClass
	public void setUp() {
		// Create request specification
		requestSpec = new RequestSpecBuilder()
				// Set content type
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", "token xxx")
				// Set base URL
				.setBaseUri("https://api.github.com")
				// build request specification
				.build();
		sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8uT7YZP0J6wxmRHTpmxL0g6pxWToDB7/doXk/AjEn9Wg3x66gKSoSF2MoCRlg/8YOsFsb4FeUP4DwQchoskBbg15rsnp+wsdUpFXp6UH/AJwfYLMIhRUX31/VGsirGZx1hvr6omiERNBUxoAw2DQLnMMGfqXzKRXqBW7tnepS4p9OKSVhkKsDtjRT851m6qtI9+BZPGm/WVNSHhZbPvv2NH1cAxs+Jqg5rY+yQc2dcA6gniVlGR6tJF7l/y8vCDvX2/AacMTFN46dwdZwbG6IWHA/GqGGkpQGj0V7j06NGURV0cyyMG9ENaZ8F2Dhwif4zoa1pcmaQ17ji/I08";

	}

	@Test(priority = 1)
	public void addSSHKey() {

		// Create a JSON request
		String reqBody = "{\"title\" : \"TestAPIKey\" , \"key\" : \"" + sshKey + "\"}";
		Response response = given().spec(requestSpec) // use requestSpec
				.body(reqBody) // add request body
				.when().post("/user/keys"); // Send POST request

		System.out.println(response.getBody().asPrettyString());
		sshKeyId = response.then().extract().path("id");
		// Assertion
		response.then().statusCode(201);
		response.then().body("id", notNullValue());

	}

	@Test(priority = 2)
	public void getSSHKey() {

		Response response = given().spec(requestSpec) // use requestSpec
				.when().get("/user/keys");
		System.out.println(response.getBody().asPrettyString());
		response.then().statusCode(200);
		response.then().body("[0].id", equalTo(sshKeyId));

	}

	@Test(priority = 3)
	public void deleteSSHKey() {
		Response response = given().spec(requestSpec).when().pathParam("keyId", sshKeyId).delete("/user/keys/{keyId}");

		// System.out.println(response.getBody().asPrettyString());
		response.then().statusCode(204);

	}

}
