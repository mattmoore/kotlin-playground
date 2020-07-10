package io.mattmoore.kotlin.plugins.codegen

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

class KotlinClassGenerator(
  private val klass: Element,
  private val className: String,
  private val packageName: String,
  private val greeting: String = "Hello!"
) {
  val generated
    get() =
      FileSpec.builder(packageName, className)
        .addType(TypeSpec.classBuilder(className)
          .addFunction(FunSpec.builder("greeting")
            .returns(String::class)
            .addStatement(
              """println("${klass.enclosedElements
                .filter { it.kind == ElementKind.FIELD }
                .map { it.simpleName.toString().replace("\$delegate", "") }}")"""
            )
            .addStatement("""return "$greeting"""")
            .build())
          .build())
        .build()
}