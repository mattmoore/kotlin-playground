# Kotlin Annotation Processor Example

This is a demonstration if annotation processing in Kotlin with Kapt and KotlinPoet.

## What's annotation processing?

Annotations allow for marking source code for manipulation at compile-time. An annotation starts with `@` followed by the annotation name, then the name of the class. For example:

```kotlin
@MyAnnotation
class MyClass {
  ...
}
```

## What's Kapt?

Kapt stands for *Kotlin Annotation Processor Tool*. It allows for creating annotation processors with Kotlin. If you're making an annotation processor, you should use kapt instead of Java AnnotationProcessor. For more info, please see the [kapt documentation](https://kotlinlang.org/docs/reference/kapt.html?_ga=2.85472923.321879745.1593895990-2087168357.1593696070).

## What's KotlinPoet?

[KotlinPoet](https://square.github.io/kotlinpoet/) is the Kotlin equivalent of JavaPoet. In a nutshell, it lets you generate Kotlin source files.
