package com.github.llmaximll.features.home

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.core.common.Category
import com.github.llmaximll.core.common.ImageCache
import com.github.llmaximll.core.common.err
import com.github.llmaximll.core.common.fetchImage
import com.github.llmaximll.core.common.log
import com.github.llmaximll.core.common.models.Article
import com.github.llmaximll.core.common.readFromDiskCache
import com.github.llmaximll.core.common.saveToDiskCache
import com.github.llmaximll.core.common.theme.Typography
import com.github.llmaximll.core.common.ui.shimmerEffect

const val routeHomeScreen = "home"

private const val CARD_HEIGHT = 190
private const val CARD_WIDTH = 130

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onArticleClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val articlesState by viewModel.articlesState.collectAsState()
    val searchText by viewModel.searchFlow.collectAsState()

    LaunchedEffect(articlesState) {
        log("HomeScreen:: articlesState: $articlesState")
    }

    Column(
        modifier = modifier
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SearchBar(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = searchText,
            onChangeText = { viewModel.changeSearchText(it) }
        )

        AnimatedContent(
            targetState = articlesState.any { (_, state) ->
                if (state is CategoryState.Success) {
                    state.articles.isNotEmpty()
                } else {
                    true
                }
            },
            label = "Content"
        ) { isContentVisible ->
            if (isContentVisible) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Category.entries
                        .sortedBy { it.name }
                        .forEach { category ->
                            item(
                                key = category.name
                            ) {
                                Articles(
                                    modifier = Modifier,
                                    category = category,
                                    state = articlesState[category] ?: CategoryState.Init,
                                    onArticleClick = onArticleClick,
                                    onShowSnackbar = onShowSnackbar
                                )
                            }
                        }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.features_home_empty_articles)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onChangeText: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = onChangeText,
        placeholder = {
            Text(
                text = stringResource(id = R.string.features_home_search_placeholder)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = text.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(
                    onClick = { onChangeText("") }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null
                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun Articles(
    modifier: Modifier = Modifier,
    category: Category,
    state: CategoryState,
    onArticleClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    Column {
        if (!(state is CategoryState.Success && state.articles.isEmpty())) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = category.name,
                style = Typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            modifier = modifier,
            targetState = state,
            label = "Articles"
        ) { categoryState ->
            when (categoryState) {
                CategoryState.Init -> ArticlesLoading()
                CategoryState.Loading -> ArticlesLoading()
                is CategoryState.Error -> {
                    val errorText = stringResource(id = R.string.features_home_download_error)

                    onShowSnackbar(errorText)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorText,
                        textAlign = TextAlign.Center
                    )
                }

                is CategoryState.Success -> {
                    ArticlesSuccess(
                        articles = categoryState.articles,
                        onArticleClick = onArticleClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticlesLoading(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { i ->
            if (i == 0) {
                Spacer(modifier = Modifier.width(6.dp))
            }

            Box(
                modifier = Modifier
                    .width(CARD_WIDTH.dp)
                    .height(CARD_HEIGHT.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            ) {

            }

            if (i == 4) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun ArticlesSuccess(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onArticleClick: (String) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(
            items = articles,
            key = { _, item -> item.hashCode() }
        ) { i, article ->
            if (i == 0) {
                Spacer(modifier = Modifier.width(6.dp))
            }

            ElevatedCard(
                modifier = Modifier
                    .width(CARD_WIDTH.dp)
                    .height(CARD_HEIGHT.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = { onArticleClick(article.url ?: "") }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val context = LocalContext.current
                    val density = LocalDensity.current.density

                    var bitmap by remember(article.urlToImage) { mutableStateOf<ImageBitmap?>(null) }
                    val errorBitmap = ImageBitmap.imageResource(id = R.drawable.error_image)

                    LaunchedEffect(article.urlToImage) {
                        val imageUrl = article.urlToImage ?: return@LaunchedEffect

                        val cachedBitmap =
                            ImageCache.get(imageUrl) ?: context.readFromDiskCache(
                                imageUrl
                            )

                        if (cachedBitmap != null) {
                            bitmap = cachedBitmap.asImageBitmap()
                        } else {
                            try {
                                val decodedBitmap = fetchImage(imageUrl)
                                val scaledBitmap = Bitmap.createScaledBitmap(
                                    decodedBitmap,
                                    (decodedBitmap.width * density).toInt() / 15,
                                    (decodedBitmap.height * density).toInt() / 15,
                                    true
                                )
                                bitmap = scaledBitmap.asImageBitmap()

                                context.saveToDiskCache(imageUrl, scaledBitmap)

                                ImageCache.put(imageUrl, scaledBitmap)
                            } catch (e: Exception) {
                                err(e)
                                bitmap = errorBitmap
                            } finally {
                                if (bitmap == null)
                                    bitmap = errorBitmap
                            }
                        }
                    }

                    // Стандартный подход
                    /*AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black
                                        )
                                    ),
                                    size = this.size,
                                )
                            },
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(article.urlToImage)
                            .crossfade(true)
                            .build(),
                        contentDescription = article.content,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.error_image),
                        alignment = Alignment.Center
                    )*/

                    AnimatedContent(
                        targetState = bitmap,
                        label = "Image",
                        transitionSpec = { fadeIn().togetherWith(fadeOut()) }
                    ) { bitmapState ->
                        if (bitmapState != null) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                                    .drawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black
                                                )
                                            ),
                                            size = this.size,
                                        )
                                    },
                                bitmap = bitmapState,
                                contentDescription = article.content,
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                                    .shimmerEffect()
                            ) {

                            }
                        }
                    }

                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.BottomCenter),
                        text = article.title ?: "",
                        color = Color.White,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        style = Typography.bodyMedium
                    )
                }
            }

            if (i == articles.lastIndex) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}