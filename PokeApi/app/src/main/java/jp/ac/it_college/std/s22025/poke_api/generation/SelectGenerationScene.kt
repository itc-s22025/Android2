package jp.ac.it_college.std.s22025.poke_api.generation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.it_college.std.s22025.poke_api.R
import jp.ac.it_college.std.s22025.poke_api.ui.theme.PokeApiTheme

@Composable
fun SelectGenerationScene(modifier: Modifier = Modifier){
    Surface(modifier) {
        LazyColumn(modifier.fillMaxWidth()){
            //itemsないと表示されなかった？かも
            //count = 2になると同じのが複数できる
            //今は第９世代のみ 将来的に全世代を表示して選択可能にしたい
            items(count = 1) {
                ItemGeneration(generation = 9, seriesName = "スカーレット/バイオレット")
            }
        }
    }
}

//ポケモンの世代とシリーズ名をだすやつ 世代: Int, シリーズ名: Stringで作成
@Composable
fun ItemGeneration(generation: Int, seriesName: String){
    Surface (
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            //文字列リソースのgenerationを渡す 第?世代
            Text(text = stringResource(id = R.string.generation, generation),
                style = MaterialTheme.typography.titleLarge)
            //シリーズ名
            Text(text = seriesName,
                style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun SelectGenerationScenePreview(){
    PokeApiTheme {
        SelectGenerationScene(Modifier.fillMaxSize())
    }
}