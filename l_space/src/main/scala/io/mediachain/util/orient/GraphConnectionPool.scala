package io.mediachain.util.orient

import gremlin.scala.Graph
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory

import scala.concurrent.{ExecutionContext, Future}

trait GraphConnectionPool {
  def getGraph: Future[Graph]
}

class OrientGraphPool(val factory: OrientGraphFactory)
  (implicit ec: ExecutionContext)
  extends GraphConnectionPool {

  /**
    * Obtains a new graph instance from the `OrientGraphFactory`'s connection
    * pool.
    * @return a `Future` that resolves to an open `Graph` connection.  Will
    *         fail if the pool is at max capacity.
    */
  def getGraph: Future[Graph] = Future {
    factory.getTx()
  }
}
