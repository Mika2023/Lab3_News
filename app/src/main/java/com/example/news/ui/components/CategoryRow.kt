package com.example.news.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.news.enums.TopHeadlineCategories

@Composable
fun CategoryRow(
    categories: Array<TopHeadlineCategories>,
    selectedCategory: TopHeadlineCategories,
    onCategorySelect: (TopHeadlineCategories) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                isSelected = category.enName == selectedCategory.enName,
                onClick = { onCategorySelect(category) }
            )
        }
    }
}