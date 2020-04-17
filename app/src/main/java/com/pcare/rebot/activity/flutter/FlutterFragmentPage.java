package com.pcare.rebot.activity.flutter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.pcare.rebot.R;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class FlutterFragmentPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter_page);
//        FlutterFragment flutterFragment = FlutterFragment.createDefault();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.fl_container,flutterFragment)
//                .commit();
//        FlutterFragment flutterFragment = FlutterFragment.withNewEngine()
//                .initialRoute("route1")
//                .build();
        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getNavigationChannel().setInitialRoute("route1");
        flutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());
        FlutterEngineCache.getInstance().put("my_engine_id",flutterEngine);

        FlutterFragment flutterFragment = FlutterFragment.withCachedEngine("my_engine_id").build();

    }
}
