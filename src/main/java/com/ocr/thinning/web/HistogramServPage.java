package com.ocr.thinning.web;

import com.google.common.collect.ImmutableList;
import com.ocr.thinning.EqualizationContainer;
import com.ocr.thinning.Histogram;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Yusfia Hafid A on 10/15/2015.
 */
public class HistogramServPage extends PubLayout {
    @Inject
    private EqualizationContainer eq;
    private FileUploadField fileUpload = new FileUploadField("fileUpload");
    private String UPLOAD_FOLDER = "C:\\";
    private static final Logger log = LoggerFactory.getLogger(HistogramServPage.class);
    public HistogramServPage(PageParameters parameters) {
        super(parameters);
        eq.setImageTest();
        eq.gammaProcessing();
        final Form<Void> form = new Form<Void>("form");
        WebMarkupContainer chartrgb = new WebMarkupContainer("chartrgb");
        chartrgb.setOutputMarkupId(true);
        WebMarkupContainer chartrgbout = new WebMarkupContainer("chartrgbout");
        chartrgbout.setOutputMarkupId(true);
        form.add(fileUpload);
        Image inputan  = new Image("inputPic", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getInput();
            }
        });
        inputan.setOutputMarkupId(true);
        form.add(inputan);
        Image outputan  = new Image("outputPic", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getOutput();
            }
        });
        outputan.setOutputMarkupId(true);
        form.add(outputan);
        final Model<Integer> tresholdModel = new Model();
        RangeTextField treshold = new RangeTextField<>("gamma",tresholdModel,Integer.class);
        treshold.setMaximum(100);
        treshold.setMinimum(0);
        treshold.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                float slider = (float)((tresholdModel.getObject() * 6) + 100) / 100;
                eq.setGammaValue(slider);
                log.info("slider value = {} ",slider);
                ajaxRequestTarget.add(outputan,chartrgbout);
            }
        });
        form.add(treshold);
        treshold.setOutputMarkupId(true);
        final LoadableDetachableModel<String> c3Model = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                eq.setHist();
                final String dataJson = eq.getHist().getJsonC3String();
                log.info("String result : {}",dataJson);
                return "var chart = c3.generate({\n" +
                        "    bindto: '#" + chartrgb.getMarkupId() + " .chart',\n" +
                        "    data: {" +
                        "        columns: [" + dataJson + "]," +
                        "        colors: {red: 'red', green: 'green', blue: 'blue',grayscale: 'gray'},\n" +
                        "        types: {grayscale: 'bar'}\n" +
                        "    }" +
                        "});";
            }
        };
        chartrgb.add(new Label("c3", c3Model).setEscapeModelStrings(false));
        form.add(chartrgb);
        final LoadableDetachableModel<String> c3ModelOut = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                eq.setHistoutput();
                final String dataJson = eq.getHistoutput().getJsonC3String();
                log.info("String result : {}",dataJson);
                return "var chart = c3.generate({\n" +
                        "    bindto: '#" + chartrgbout.getMarkupId() + " .chart',\n" +
                        "    data: {" +
                        "        columns: [" + dataJson + "]," +
                        "        colors: {red: 'red', green: 'green', blue: 'blue',grayscale: 'gray'},\n" +
                        "        types: {grayscale: 'bar'}\n" +
                        "    }" +
                        "});";
            }
        };
        chartrgbout.add(new Label("c3out", c3ModelOut).setEscapeModelStrings(false));
        form.add(chartrgbout);
        form.add(new LaddaAjaxButton("klik",new Model<>("Set Image and Process"), Buttons.Type.Default){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {
                    log.info("masuk setelah upload");
                    eq.setImage(uploadedFile.getBytes());
                    target.add(inputan,chartrgb,treshold);
                }
            }
        });
        add(form);
    }

    @Override
    public IModel<String> getTitleModel() {
        return new Model<>("Histogram Equalization");
    }

    @Override
    public IModel<String> getMetaDescriptionModel() {
        return new Model<>("Histogram Equalization");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        final WebjarsJavaScriptResourceReference D3_JS = new WebjarsJavaScriptResourceReference("d3/current/d3.js");
        final WebjarsCssResourceReference C3_CSS = new WebjarsCssResourceReference("c3/current/c3.css");
        final WebjarsJavaScriptResourceReference C3_JS = new WebjarsJavaScriptResourceReference("c3/current/c3.js") {
            @Override
            public List<HeaderItem> getDependencies() {
                return ImmutableList.of(
                        CssHeaderItem.forReference(C3_CSS),
                        JavaScriptHeaderItem.forReference(D3_JS));
            }
        };
        response.render(JavaScriptHeaderItem.forReference(C3_JS));
    }

    public byte[] getInput(){
        return eq.getInput();
    }

    public byte[] getOutput() {
        eq.cdfProcessing();
        return eq.getOutput();
    }
}