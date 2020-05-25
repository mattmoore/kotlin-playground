package io.mattmoore.kotlin.playground.recursion

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class RecursionSpec : DescribeSpec({
  data class Node(var value: Int, var next: Node?)

  val list = Node(1, Node(2, Node(3, null)))

  describe("apply operation to list recursively") {
    fun recurse(node: Node?, f: (Int) -> Int): Node {
      fun descend(node: Node?): Unit {
        if (node == null) return
        node.value = f(node.value)
        if (node.next == null) return
        return descend(node.next)
      }
      return node!!
    }

    it("should iterate through each element") {
      recurse(list) { it + 1 } shouldBe Node(2, Node(3, Node(4, null)))
    }
  }

  describe("reverse list recursively") {
    fun reverseNonEmptyListRecursive(node: Node?): Node? {
      return when {
        node == null -> node
        node.next == null -> node
        else -> {
//          val nextNode = node.next
//          val nodeValue = node.value
//          node.value = nextNode?.value
//          nextNode?.value = nodeValue
//          reverseNonEmptyListRecursive(node)
          val newNode = reverseNonEmptyListRecursive(node.next as Node)
          node.next?.next = node
          newNode
        }
      }
    }

    val expected = Node(3, Node(2, Node(1, null)))

//    it("should reverse all the elements") {
//      reverseNonEmptyListRecursive(list) shouldBe expected
//    }
  }

})
