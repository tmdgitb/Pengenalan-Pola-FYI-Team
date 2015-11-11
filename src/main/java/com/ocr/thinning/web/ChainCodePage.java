package com.ocr.thinning.web;

/**
 * Created by Yusfia Hafid A on 10/16/2015.
 */

import com.ocr.thinning.ChainCodeContainer;
import com.ocr.thinning.ThinningAlgorithmContainer;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;

import javax.inject.Inject;


/**
 * Created by Yusfia Hafid A on 10/15/2015.
 */
public class ChainCodePage extends PubLayout {

    private FileUploadField fileUpload = new FileUploadField("fileUpload");
    private String UPLOAD_FOLDER = "C:\\";
    @Inject
    private ChainCodeContainer cncd;
    public ChainCodePage(PageParameters parameters) {
        super(parameters);
        cncd.setInput("no.jpg");
        cncd.chainCodeProcessingStep1(125);
        cncd.chainCodeProcessingStep2();
        final Form<Void> form = new Form<Void>("form");
        MultiLineLabel multilabelmod = new MultiLineLabel("multiLineLabel", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject(){
                return getTextLabel();
            }
        });
        form.add(multilabelmod);
        multilabelmod.setOutputMarkupId(true);
        Image inputan  = new Image("inputImage", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getInput();
            }
        });
        Image outputan = new Image("outputImage", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getOutput();
            }
        });
        final Model<Integer> tresholdModel = new Model();
        RangeTextField treshold = new RangeTextField<>("treshold",tresholdModel,Integer.class);
        treshold.setMaximum(255);
        treshold.setMinimum(0);
        treshold.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                cncd.chainCodeProcessingStep1(tresholdModel.getObject());
                cncd.chainCodeProcessingStep2();
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
        form.add(new LaddaAjaxButton("klik",new Model<>("Convert to Binary Image"), Buttons.Type.Default){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {
                    cncd.setInput(uploadedFile.getBytes());
                    cncd.chainCodeProcessingStep1(125);
                    cncd.chainCodeProcessingStep2();
                    target.add(inputan,outputan,multilabelmod,treshold);
                }
            }
        });
        add(form);
    }

    @Override
    public IModel<String> getTitleModel() {
        return new Model<>("Chain Code");
    }

    @Override
    public IModel<String> getMetaDescriptionModel() {
        return new Model<>("Chain Code");
    }

    public byte[] getInput(){
        return cncd.getInput();
    }

    public byte[] getOutput(){
        return cncd.getOutput();
    }

    public String getTextLabel(){
        return cncd.getTextResult();
    }

}