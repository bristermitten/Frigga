package me.bristermitten.frigga.types

import StringType
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.type.*
import org.junit.jupiter.api.Test

class TypeRelationshipTests {

    @Test
    fun `Test Correct Type Relationships between Number Types`() {
        IntType relationshipTo DecType shouldBe TypeRelationship.Sibling

        IntType relationshipTo NumType shouldBe TypeRelationship.Subtype

        DecType relationshipTo NumType shouldBe TypeRelationship.Subtype

        NumType relationshipTo NumType shouldBe TypeRelationship.Same
        NumType relationshipTo IntType shouldBe TypeRelationship.Supertype
    }

    @Test
    fun `Test Correct Type Relationships between Other Types`() {
        StringType relationshipTo DecType shouldBe TypeRelationship.NoRelationship
        StringType relationshipTo AnyType shouldBe TypeRelationship.Subtype
        AnyType relationshipTo StringType shouldBe TypeRelationship.Supertype

        StringType relationshipTo NothingType shouldBe TypeRelationship.Supertype
        NothingType relationshipTo StringType shouldBe TypeRelationship.Subtype
    }
}
