package com.lnrs.clamav.prototype;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUploadHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String bucketName = "protoclamavserverlessstack-rbucketdeb6e181-crhdaxau8ude";
        String key = "uploaded-file-" + System.currentTimeMillis();

        try {
            // Decode the base64 encoded file content
            byte[] fileContent = Base64.getDecoder().decode(request.getBody());

            // Create metadata for the file
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileContent.length);

            // Upload the file to S3
            InputStream inputStream = IOUtils.toInputStream(new String(fileContent), "UTF-8");
            s3Client.putObject(bucketName, key, inputStream, metadata);

            // Return success response
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(200);
            response.setBody("File uploaded successfully");
            return response;
        } catch (IOException e) {
            context.getLogger().log("Error uploading file: " + e.getMessage());
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(500);
            response.setBody("Error uploading file");
            return response;
        }
    }
}

 /** In this code, we first decode the base64 encoded file content from the request body. We then create an  ObjectMetadata  object to specify the content length of the file. Finally, we upload the file to the S3 bucket using the  putObject  method of the S3 client.
 The Lambda function returns a success response if the file is uploaded successfully, and an error response if there is an exception during the upload process.
 Step 4: Deploy the Lambda Function
 To deploy the Lambda function, you can use the AWS CLI or the AWS Management Console.
 Using the AWS CLI:
 aws lambda create-function --function-name FileUploadHandler --runtime java11 --handler com.lnrs.clamav.prototype.FileUploadHandler::handleRequest --role arn:aws:iam::123456789012:role/lambda-role --zip-file fileb://target/lambda-function-1.0-SNAPSHOT.jar --timeout 30 --memory-size 512

 Using the AWS Management Console:

 Go to the  Lambda console.
 Click on the  Create function  button.
 Enter a name for the function (e.g.,  FileUploadHandler ).
 Select  Java 11  as the runtime.
 Choose an existing role or create a new role with the necessary permissions.
 Upload the JAR file containing the Lambda function code.
 Set the handler to  com.lnrs.clamav.prototype.FileUploadHandler::handleRequest .
 Set the memory and timeout settings as required.
 Click on the  Create function  button.

 Step 5: Test the Lambda Function
 To test the Lambda function, you can use the AWS Management Console or the AWS CLI.
 Using the AWS Management Console:

 Go to the  Lambda console.
 Click on the function you created (e.g.,  FileUploadHandler ).
 Click on the  Test  button.
 Enter a name for the test event.
 Enter the base64 encoded file content in the request body.
 Click on the  Create  button.
 Click on the  Test  button again to run the test.

 Using the AWS CLI:
 aws lambda invoke --function-name FileUploadHandler --payload '{"body": "base64-encoded-file-content"}' output.json

 Step 6: Configure the API Gateway
 To expose the Lambda function as an API endpoint, you can use the AWS Management Console or the AWS CLI.
 Using the AWS Management
  **/