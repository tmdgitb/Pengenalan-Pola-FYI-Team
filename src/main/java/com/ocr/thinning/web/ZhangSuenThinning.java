package com.ocr.thinning.web;

import com.ocr.thinning.ThinningAlgorithmContainer;
import com.ocr.thinning.ZhangSuen;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;

import javax.inject.Inject;
import java.io.File;

/**
 * Created by Yusfia Hafid A on 10/15/2015.
 */
public class ZhangSuenThinning extends PubLayout {
    @Inject
    private ThinningAlgorithmContainer zn ;
    private FileUploadField fileUpload = new FileUploadField("fileUpload");
    private String UPLOAD_FOLDER = "C:\\";

    public ZhangSuenThinning(PageParameters parameters) {
        super(parameters);
        zn.setInput("A.jpg");
        final Form<Void> form = new Form<Void>("form");
        Image inputan  = new Image("inputZhangSuen", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getInput();
            }
        });
        Image outputan = new Image("outputZhangSuen", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getOutput();
            }
        });
        Label markupLabel = new Label("markupLabel", new AbstractReadOnlyModel<Integer>() {
            @Override
            public Integer getObject() {
                return zn.getHist().uniqueColor;
            }
        });

        MultiLineLabel multilabelmod = new MultiLineLabel("multiLineLabel", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject(){
                return getTextLabel();
            }
        });
        form.add(multilabelmod);
        multilabelmod.setOutputMarkupId(true);

        final Model<Integer> tresholdModel = new Model();
        RangeTextField treshold = new RangeTextField<Integer>("treshold",tresholdModel,Integer.class);
        treshold.setMaximum(255);
        treshold.setMinimum(0);
        treshold.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                zn.setBinnaryTreshold(tresholdModel.getObject());
                zn.setOutput();
                ajaxRequestTarget.add(outputan,multilabelmod);
            }
        });
        form.add(treshold);
        treshold.setOutputMarkupId(true);
        form.add(inputan);
        inputan.setOutputMarkupId(true);
        form.add(outputan);
        outputan.setOutputMarkupId(true);
        form.add(fileUpload);
        form.add(markupLabel);
        markupLabel.setOutputMarkupId(true);

        form.add(new LaddaAjaxButton("klik",new Model<>("Convert to Skeleton"), Buttons.Type.Default){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {
                    zn.setInput(uploadedFile.getBytes());
                    target.add(inputan,outputan,markupLabel,treshold,multilabelmod);
                }
            }
        });
        /*
        form.add(new LaddaAjaxButton("klik",new Model<>("Convert to Skeleton"), Buttons.Type.Default){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {
                    zn.setInput(uploadedFile.getBytes());
                    target.add(inputan,outputan,markupLabel,treshold,multilabelmod);
                }
            }
        });
        */
        add(form);
    }

    public String getTextLabel(){
        return zn.recognizeObject();
    }

    public byte[] getInput(){
        return zn.getInput();
    }

    public byte[] getOutput(){
        zn.setOutput();
        return zn.getOutput();
    }

    @Override
    public IModel<String> getTitleModel() {
        return new Model<>("Zhang Suen Thinning Algorithm");
    }

    @Override
    public IModel<String> getMetaDescriptionModel() {
        return new Model<>("Zmhang Suen Thinning Algorithm");
    }

}