package org.apache.taverna.mobile.ui.imagezoom;


import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;

import org.apache.taverna.mobile.R;
import org.apache.taverna.mobile.data.DataManager;
import org.apache.taverna.mobile.data.local.DBHelper;
import org.apache.taverna.mobile.ui.base.BasePresenter;
import org.apache.taverna.mobile.utils.SvgDecoder;
import org.apache.taverna.mobile.utils.SvgDrawableTranscoder;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageZoomPresenter extends BasePresenter<ImageZoomMvpView> {

    public static final String NO_IMAGE_URI = "NO Image Found";
    public static final String DB_ERROR = "There is some problem. Please try after sometime ";
    private DataManager mDataManager;
    private Subscription mSubscriptions;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public ImageZoomPresenter(DataManager dataManager) {

        mDataManager = dataManager;

    }

    @Override
    public void attachView(ImageZoomMvpView mvpView) {

        super.attachView(mvpView);

        requestBuilder = Glide.with(getMvpView().getAppContext())
                .using(Glide.buildStreamModelLoader(Uri.class,
                        getMvpView().getAppContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .animate(android.R.anim.fade_in);

    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void loadImage(String id, final ImageView imageView) {

        if (mSubscriptions != null) mSubscriptions.unsubscribe();

        mSubscriptions = mDataManager.getImageURI(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorSnackBar(DB_ERROR);
                    }

                    @Override
                    public void onNext(Map<String, String> imageURI) {
                        if (imageURI.size() != 0) {

                            setSVG(imageURI, imageView);

                            getMvpView().setJPGuri(imageURI.get(DBHelper.JPG_URI));
                        } else {

                            getMvpView().showErrorSnackBar(NO_IMAGE_URI);
                        }
                    }
                });
    }

    private void setSVG(final Map<String, String> imageURI, final ImageView imageView) {

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(Uri.parse(imageURI.get(DBHelper.SVG_URI)))
                .into(new Target<PictureDrawable>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        imageView.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        imageView.setImageDrawable(errorDrawable);

                        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
                            imageView.setLayerType(ImageView.LAYER_TYPE_NONE, null);
                        }

                        getMvpView().setJPGImage();
                    }

                    @Override
                    public void onResourceReady(PictureDrawable resource,
                                                GlideAnimation<? super PictureDrawable>
                                                        glideAnimation) {
                        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
                            imageView.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
                        }
                        imageView.setImageDrawable(resource);
                        getMvpView().addImageAttacher();
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {

                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {

                    }

                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void setRequest(Request request) {

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });

    }


}
