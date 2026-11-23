package poc.provenance.sample

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

/** Stand-in for a personal app that is built and attested by the workflow. */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val padding = (24 * resources.displayMetrics.density).toInt()
        setContentView(
            TextView(this).apply {
                text = "출처 증명이 붙은 샘플 앱입니다.\n\n이 앱은 아무 권한도 요청하지 않습니다."
                textSize = 18f
                setPadding(padding, padding, padding, padding)
            }
        )
    }
}
