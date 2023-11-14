package jp.ac.it_college.std.s22025.poke_api.title

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.it_college.std.s22025.poke_api.R
import jp.ac.it_college.std.s22025.poke_api.ui.theme.PokeApiTheme

@Composable
fun TitleScene(modifier: Modifier = Modifier){
    Surface(modifier) {
        Column(
            //中央にtextAline = center
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            //直接テキスト書いてもいいけどそうすると多言語対応できなくなる->文字列リソースからとってくる
            //-> stringResource関数 R.stringはimport
            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun TitleScenePreview(){
    PokeApiTheme {
        TitleScene(Modifier.fillMaxSize())
    }
}