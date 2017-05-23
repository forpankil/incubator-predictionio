/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.predictionio.data.storage.azurefs

import java.io.File

import grizzled.slf4j.Logging
import org.apache.predictionio.data.storage.BaseStorageClient
import org.apache.predictionio.data.storage.StorageClientConfig
import org.apache.predictionio.data.storage.StorageClientException
import com.microsoft.azure.storage.CloudStorageAccount

class StorageClient(val config: StorageClientConfig) extends BaseStorageClient
    with Logging {
  override val prefix = "AzureFS"
  val connectionString = "DefaultEndpointsProtocol=https;AccountName=pdoshitest01;AccountKey=73gvDRgzVa2e4zpxyWI5qiwm2iRLnC9g3+uuljLTT9E74Up4aQnrOzt2daNER1XHUALZ0ki6tz7uXo2YR7B7kg==;EndpointSuffix=core.windows.net"
  val storageAccount  = CloudStorageAccount.parse(connectionString);
  val blobClient = storageAccount.createCloudBlobClient();
  val container = blobClient.getContainerReference("predictionio");
  val client = container
}