/**
 * Licensed to Big Data Genomics (BDG) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The BDG licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bdgenomics.adam.cli

import org.apache.spark.SparkContext
import org.bdgenomics.adam.rdd.ADAMContext._
import org.bdgenomics.utils.cli._
import org.kohsuke.args4j.{ Argument, Option ⇒ Args4jOption }

object Features2ADAM extends BDGCommandCompanion {
  val commandName = "features2adam"
  val commandDescription = "Convert a file with sequence features into corresponding ADAM format"

  def apply(cmdLine: Array[String]) = {
    new Features2ADAM(Args4j[Features2ADAMArgs](cmdLine))
  }
}

class Features2ADAMArgs extends Args4jBase with ParquetSaveArgs {
  @Argument(required = true, metaVar = "FEATURES",
    usage = "The features file to convert (e.g., .bed, .gff)", index = 0)
  var featuresFile: String = _
  @Argument(required = true, metaVar = "ADAM",
    usage = "Location to write ADAM features data", index = 1)
  var outputPath: String = null
  @Args4jOption(required = false, name = "-num_partitions",
    usage = "Number of partitions to load an interval file using.")
  var numPartitions: Int = _
}

class Features2ADAM(val args: Features2ADAMArgs)
    extends BDGSparkCommand[Features2ADAMArgs] {
  val companion = Features2ADAM

  def run(sc: SparkContext) {
    sc.loadFeatures(args.featuresFile, None, Option(args.numPartitions)).saveAsParquet(args)
  }
}
