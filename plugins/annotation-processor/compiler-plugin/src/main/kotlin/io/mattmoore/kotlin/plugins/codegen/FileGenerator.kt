package io.mattmoore.kotlin.plugins.codegen

import com.google.auto.service.AutoService
import io.mattmoore.kotlin.plugins.annotations.Greeter
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(FileGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class FileGenerator : AbstractProcessor() {
  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(Greeter::class.java.name)
  }

  override fun getSupportedSourceVersion(): SourceVersion {
    return SourceVersion.latest()
  }

  override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
    roundEnvironment?.getElementsAnnotatedWith(Greeter::class.java)
      ?.forEach {
        val className = it.simpleName.toString()
        val pack = processingEnv.elementUtils.getPackageOf(it).toString()
        generateClass(it, className, pack)
      }
    return true
  }

  private fun generateClass(klass: Element, className: String, pack: String) {
    val fileName = "Generated${className}"
    val file = KotlinClassGenerator(klass, fileName, pack).generated
    val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
    file.writeTo(File("${kaptKotlinGeneratedDir}/$fileName.kt"))
  }

  companion object {
    const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
  }
}