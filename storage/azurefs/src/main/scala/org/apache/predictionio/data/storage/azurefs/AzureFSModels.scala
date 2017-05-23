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
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileInputStream

import grizzled.slf4j.Logging
import org.apache.predictionio.data.storage.Model
import org.apache.predictionio.data.storage.Models
import org.apache.predictionio.data.storage.StorageClientConfig

import scala.io.Source
import com.microsoft.azure.storage.blob.CloudBlobContainer


class AzureFSModels(container: CloudBlobContainer, config: StorageClientConfig, prefix: String)
  extends Models with Logging {

  def insert(i: Model): Unit = {
    try {
      debug("inserting model to blob store")
      val blob = container.getBlockBlobReference(i.id)
      println("Model ID " + i.id);
      val filename = "/tmp/"+i.id+"-insert.tmp"
      
      val fos = new FileOutputStream(new File(filename))
      fos.write(i.models)
      fos.close
      println("File created in tmp");
      val source = new File(filename);
      blob.upload(new FileInputStream(source),source.length());
      println("Uploaded to blob store");
    } catch {
      case e: FileNotFoundException => error(e.getMessage)
    }
  }

  def get(id: String): Option[Model] = {
    try {
      debug("getting model from blob store")
      val blob = container.getBlockBlobReference(id)
      println("Getting Model ID from blob store " + id);
      val destinationFile = new File("/tmp/"+id+"-get.tmp");
      blob.downloadToFile(destinationFile.getAbsolutePath());
      
      Some(Model(
        id = id,
        models = Source.fromFile(destinationFile)(
          scala.io.Codec.ISO8859).map(_.toByte).toArray))
    } catch {
      case e: Throwable =>
        error(e.getMessage)
        None
    }
  }

  def delete(id: String): Unit = {
    debug("deleting model from blob store")
    val blob = container.getBlockBlobReference(id)
    // Delete the blob.
    blob.deleteIfExists();
  }
}
