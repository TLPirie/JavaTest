package com.example.javatestar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private AnchorNode mAnchorNode;

    ArFragment arFragment;
    ModelRenderable lionRenderable;
    private AnchorNode anchorNode;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //Removes the bootup handmotion animation. This wasn't really necessary but it annoyed me lol.
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        ModelRenderable.builder()
                .setSource(this, Uri.parse("Duck_01.sfb"))
                .build()
                .thenAccept(renderable -> lionRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        arFragment.getArSceneView().getScene().setOnUpdateListener(this::onSceneUpdate);

       /* arFragment.setOnTapArPlaneListener(
                (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {

                    if (lionRenderable == null){
                        return;
                    }*/

                    /*Anchor anchor = hitresult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    Node lamp = new Node(); //arFragment.getTransformationSystem : TransformableNode
                    lamp.setParent(anchorNode);
                    lamp.setRenderable(lionRenderable);*/
                    //lamp.select();

                    /*lamp.setOnTapListener((v, event) -> {
                        arFragment.getArSceneView().getScene().removeChild(lamp);
                        lamp.setRenderable(null);
                        anchor.detach();
                        lamp.setParent(null);
                        *//*TextView textView = (TextView) findViewById(R.id.textView);
                        textView.setText("something, lol");*//*
                            }
                    );*/

                }
        //);
    //}

    private void onSceneUpdate(FrameTime frameTime) {
        // Let the fragment update its state first.
        arFragment.onUpdate(frameTime);

        // If there is no frame then don't process anything.
        if (arFragment.getArSceneView().getArFrame() == null) {
            return;
        }

        // If ARCore is not tracking yet, then don't process anything.
        if (arFragment.getArSceneView().getArFrame().getCamera().getTrackingState() != TrackingState.TRACKING) {
            return;
        }

        spawnModel();
        // Place the anchor 1m in front of the camera if anchorNode is null.
        /*if (this.anchorNode == null) {
            Session session = arFragment.getArSceneView().getSession();

            *//*int randPos1, randPos2, randPos3;
            randPos1 = new Random().nextInt(3);
            randPos2 = new Random().nextInt(3 + 1) -1; //??
            //randPos3 = new Random().nextInt(0);*//*

            float[] pos = {0,0, -1}; //across, up, far
            float[] rotation = {0,0,0,1};
            Anchor anchor =  session.createAnchor(new Pose(pos, rotation));
            anchorNode = new AnchorNode(anchor);
            anchorNode.setRenderable(lionRenderable);
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            anchorNode.setOnTapListener((v, event) -> {
                arFragment.getArSceneView().getScene().removeChild(anchorNode);
                anchorNode.setRenderable(null);
                anchor.detach();
                anchorNode.setParent(null);
            });
        }*/
    }

    public void spawnModel() {
        if (this.anchorNode == null) {
            Session session = arFragment.getArSceneView().getSession();

            int randPos1, randPos2, randPos3;
            //randPos1 = new Random().nextInt(3);
            //randPos2 = new Random().nextInt(3 + 1) -1; //??
            //randPos3 = new Random().nextInt(0);

            float[] pos = {0,0, -1}; //across, up, far
            float[] rotation = {0,0,0,1};
            Anchor anchor =  session.createAnchor(new Pose(pos, rotation));
            anchorNode = new AnchorNode(anchor);
            anchorNode.setRenderable(lionRenderable);
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            anchorNode.setOnTapListener((v, event) -> {
                arFragment.getArSceneView().getScene().removeChild(anchorNode);
                anchorNode.setRenderable(null);
                anchor.detach();
                anchorNode.setParent(null);
            });

            spawnModel();
        }

    }


    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;    }
            String openGlVersionString =
                    ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                            .getDeviceConfigurationInfo()
                            .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;    }
            return true;}
}
