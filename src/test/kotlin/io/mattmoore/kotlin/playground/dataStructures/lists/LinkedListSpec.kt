package io.mattmoore.kotlin.playground.dataStructures.lists

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LinkedListSpec : DescribeSpec({
  describe("naive linked list with node") {
    class Node(var value: Int, var next: Node?) {
      fun add(newValue: Int): Node? {
        this.next = Node(newValue, null)
        return this.next
      }
    }

    var first = Node(1, null)
    val second = first.add(2)
    val third = second?.add(3)

    it("can add values") {
      first.value shouldBe 1
      first.next?.value shouldBe 2
      first.next?.next?.value shouldBe 3
    }
  }

  describe("better linked list") {
    data class Node(var value: Int, var next: Node?)

    class LinkedList {
      var root: Node? = null
      var last: Node? = null
      var size = 0

      fun add(newValue: Int) {
        if (root != null) {
          last?.next = Node(newValue, null)
          last = last?.next
        } else {
          root = Node(newValue, null)
          last = root
        }
        ++size
      }

      fun first(): Int? {
        return root?.value
      }

      fun last(): Int? {
        return last?.value
      }

      fun get(index: Int): Int? {
        var currentNode = root
        for (i in 1..index) {
          currentNode = currentNode?.next
        }
        return currentNode?.value
      }
    }

    it("can add values") {
      var ll = LinkedList()
      ll.size shouldBe 0

      ll.add(1)
      ll.size shouldBe 1

      ll.add(2)
      ll.size shouldBe 2

      ll.add(3)
      ll.size shouldBe 3

      ll.first() shouldBe 1
      ll.last() shouldBe 3

      ll.get(0) shouldBe 1
      ll.get(1) shouldBe 2
      ll.get(2) shouldBe 3
    }
  }
})