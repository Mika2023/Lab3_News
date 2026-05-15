package com.example.news.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.news.config.AppConfig
import com.example.news.data.dto.NewsArticle
import androidx.core.net.toUri
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.news.ui.theme.AccentBlue
import com.example.news.ui.theme.DividerColor
import com.example.news.ui.theme.TextPrimaryLight
import com.example.news.ui.theme.TextSecondaryLight
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MainNewsCard(article: NewsArticle) {
    val context = LocalContext.current

    val formattedDate = remember(article.publishedAt) {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            parser.parse(article.publishedAt)?.let { formatter.format(it) } ?: article.publishedAt
        } catch (e: Exception) {
            article.publishedAt.take(10)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, DividerColor)
    ) {
        Column {
            if (!article.urlToImage.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = article.urlToImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val state = painter.state
                    when (state) {
                        is AsyncImagePainter.State.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .background(DividerColor)
                            )
                        }
                        is AsyncImagePainter.State.Success -> {
                            SubcomposeAsyncImageContent(
                                modifier = Modifier.height(180.dp)
                            )
                        }
                        else -> {
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = TextPrimaryLight
                )

                Spacer(modifier = Modifier.height(4.dp))

                val authorText = article.author?.let { "$it • " } ?: ""
                Text(
                    text = "$authorText$formattedDate",
                    fontSize = 12.sp,
                    color = TextSecondaryLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!article.description.isNullOrBlank()) {
                    Text(
                        text = article.description,
                        fontSize = 14.sp,
                        color = TextSecondaryLight,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Читать в источнике ->",
                    color = AccentBlue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, article.url?.toUri())
                            context.startActivity(intent)
                        }
                        .padding(4.dp)
                )
            }
        }
    }
}