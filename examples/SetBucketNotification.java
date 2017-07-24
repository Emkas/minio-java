/*
 * Minio Java SDK for Amazon S3 Compatible Cloud Storage, (C) 2017 Minio, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.messages.NotificationConfiguration;
import io.minio.messages.TopicConfiguration;
import io.minio.messages.EventType;
import io.minio.messages.Filter;

import io.minio.errors.MinioException;

public class SetBucketNotification {
  /**
   * MinioClient.setBucketNotification() example.
   */
  public static void main(String[] args)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
    try {
      /* play.minio.io for test and development. */
      MinioClient minioClient = new MinioClient("https://play.minio.io:9000", "Q3AM3UQ867SPQQA43P2F",
                                                "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");

      /* Amazon S3: */
      // MinioClient minioClient = new MinioClient("https://s3.amazonaws.com", "YOUR-ACCESSKEYID",
      //                                           "YOUR-SECRETACCESSKEY");

      // Get current notification configuration.
      NotificationConfiguration notificationConfiguration = minioClient.getBucketNotification("my-bucketname");

      // Add a new topic configuration.
      List<TopicConfiguration> topicConfigurationList = notificationConfiguration.topicConfigurationList();
      TopicConfiguration topicConfiguration = new TopicConfiguration();
      topicConfiguration.setTopic("arn:aws:sns:us-west-2:444455556666:sns-topic-xyz");

      List<EventType> eventList = new LinkedList<>();
      eventList.add(EventType.OBJECT_CREATED_PUT);
      eventList.add(EventType.OBJECT_CREATED_COPY);
      topicConfiguration.setEvents(eventList);

      Filter filter = new Filter();
      filter.setPrefixRule("images");
      filter.setSuffixRule("pg");
      topicConfiguration.setFilter(filter);

      topicConfigurationList.add(topicConfiguration);
      notificationConfiguration.setTopicConfigurationList(topicConfigurationList);

      // Set updated notification configuration.
      minioClient.setBucketNotification("my-bucketname", notificationConfiguration);
      System.out.println("Bucket notification is set successfully");
    } catch (MinioException e) {
      System.out.println("Error occurred: " + e);
    }
  }
}