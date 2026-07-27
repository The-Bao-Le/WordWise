package com.thebaole.wordwise.data.local.seed

import com.thebaole.wordwise.data.local.entity.WordEntity

object StarterVocabulary {

    val words = listOf(
        WordEntity(
            term = "ambiguous",
            definition = "Having more than one possible meaning.",
            exampleSentence =
                "The ambiguous instruction confused the class."
        ),
        WordEntity(
            term = "collaborate",
            definition = "To work together toward a shared goal.",
            exampleSentence =
                "The students collaborate on their group project."
        ),
        WordEntity(
            term = "concise",
            definition = "Clear and brief without unnecessary detail.",
            exampleSentence =
                "She gave a concise explanation of the problem."
        ),
        WordEntity(
            term = "diligent",
            definition = "Showing careful and persistent effort.",
            exampleSentence =
                "The diligent student checked every reference."
        ),
        WordEntity(
            term = "feasible",
            definition = "Possible and practical to accomplish.",
            exampleSentence =
                "The team selected the most feasible solution."
        ),
        WordEntity(
            term = "infer",
            definition = "To reach a conclusion using evidence.",
            exampleSentence =
                "We can infer the cause from the test results."
        ),
        WordEntity(
            term = "mitigate",
            definition = "To reduce the seriousness of something.",
            exampleSentence =
                "Encryption helps mitigate the risk of data theft."
        ),
        WordEntity(
            term = "persistent",
            definition = "Continuing despite difficulty or delay.",
            exampleSentence =
                "Her persistent effort eventually solved the issue."
        ),
        WordEntity(
            term = "relevant",
            definition = "Closely connected to the current subject.",
            exampleSentence =
                "Include only evidence relevant to the argument."
        ),
        WordEntity(
            term = "verify",
            definition = "To check that something is accurate.",
            exampleSentence =
                "Always verify the source before using its data."
        )
    )
}