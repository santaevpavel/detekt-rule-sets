package ru.santaev.detekt_rule_set.utils

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.environment.setIdeaIoUseFallback
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.openapi.util.text.StringUtilRt
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory

class KtFileParser {

    private val psiFileFactory: PsiFileFactory =
        PsiFileFactory.getInstance(createKotlinCoreEnvironment().project)

    fun parseString(content: String): KtFile {
        return psiFileFactory.createFileFromText(
            "",
            KotlinLanguage.INSTANCE,
            StringUtilRt.convertLineSeparators(content)
        ) as KtFile
    }
}

class KtElementParser {

    private val psiFileFactory: KtPsiFactory = KtPsiFactory(createKotlinCoreEnvironment().project)

    fun parseString(content: String): KtBlockExpression {
        return psiFileFactory.createBlock(StringUtilRt.convertLineSeparators(content))
    }
}

private fun createKotlinCoreEnvironment(
    configuration: CompilerConfiguration = CompilerConfiguration()
): KotlinCoreEnvironment {
    // https://github.com/JetBrains/kotlin/commit/2568804eaa2c8f6b10b735777218c81af62919c1
    setIdeaIoUseFallback()
    configuration.put(
        CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
        PrintingMessageCollector(
            System.err,
            MessageRenderer.PLAIN_FULL_PATHS,
            false
        )
    )
    return KotlinCoreEnvironment.createForProduction(
        Disposer.newDisposable(),
        configuration,
        EnvironmentConfigFiles.JVM_CONFIG_FILES
    )
}