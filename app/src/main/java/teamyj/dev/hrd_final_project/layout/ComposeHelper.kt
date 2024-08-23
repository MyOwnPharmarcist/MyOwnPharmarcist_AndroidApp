package teamyj.dev.hrd_final_project.layout

import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

fun Fragment.setBottomSheetContent(composeView: ComposeView) {
    composeView.setContent {
        YourAppTheme {
            BottomSheetContent()
        }
    }
}