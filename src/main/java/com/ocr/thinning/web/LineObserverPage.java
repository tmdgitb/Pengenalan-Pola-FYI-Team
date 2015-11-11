package com.ocr.thinning.web;

import com.google.common.collect.ImmutableList;
import com.ocr.thinning.EqualizationContainer;
import com.ocr.thinning.LineObserverContainer;
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
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.markup.html.form.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Yusfia Hafid A on 11/9/2015.
 */
public class LineObserverPage extends PubLayout {
    private static final Logger log = LoggerFactory.getLogger(LineObserverPage.class);
    @Inject
    private LineObserverContainer eq;
    private FileUploadField fileUpload = new FileUploadField("fileUpload");
    private String UPLOAD_FOLDER = "C:\\";
    private int line = 0;

    public LineObserverPage(PageParameters parameters) {
        super(parameters);
        eq.setInput("A.jpg");
        eq.getJSonC3String(0);

        final Form<Void> form = new Form<Void>("form");
        // Spinner //
        final NumberTextField<Integer> stepper = new NumberTextField<Integer>("stepper", Model.of(1));
        stepper.setMinimum(0);
        stepper.setMaximum(100);
        form.add(stepper);
        WebMarkupContainer chartrgb = new WebMarkupContainer("chartrgb");
        chartrgb.setOutputMarkupId(true);
        form.add(fileUpload);
        Image inputan = new Image("inputPic", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getInput();
            }
        });
        inputan.setOutputMarkupId(true);
        form.add(inputan);
        final LoadableDetachableModel<String> c3Model = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                final String dataJson = eq.getJSonC3String(line);
                log.info("String result : {}", dataJson);
                return "var chart = c3.generate({\n" +
                        "    bindto: '#" + chartrgb.getMarkupId() + " .chart',\n" +
                        "    data: {" +
                        "        columns: [" + dataJson + "]," +
                        "        colors: {data: 'gray'},\n" +
                        "        types: {data: 'bar'}\n" +
                        "    }" +
                        "});";
            }
        };
        Label markupLabel = new Label("markupLabel", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return eq.getLine()+" Height : "+eq.getMaxRow()+" Width : "+eq.getMaxCol() ;
            }
        });
        form.add(markupLabel);
        markupLabel.setOutputMarkupId(true);
        chartrgb.add(new Label("c3", c3Model).setEscapeModelStrings(false));
        form.add(chartrgb);
        form.add(new LaddaAjaxButton("klik", new Model<>("Set Gambar"), Buttons.Type.Default) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                final FileUpload uploadedFile = fileUpload.getFileUpload();
                if (uploadedFile != null) {
                    line = 0;
                    log.info("masuk setelah upload");
                    eq.setInput(uploadedFile.getBytes());
                    target.add(chartrgb, inputan,  markupLabel);
                }
            }
        });

        form.add(new LaddaAjaxButton("previous", new Model<>("<<"), Buttons.Type.Default) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                log.info("masuk setelah upload: " + stepper.getModelObject());
                if (line - stepper.getModelObject() >= 0 && line - stepper.getModelObject() <= eq.getMaxRow() - 1)
                    line = line - stepper.getModelObject();
                target.add(chartrgb,inputan, markupLabel);
            }
        });

        form.add(new LaddaAjaxButton("next", new Model<>(">>"), Buttons.Type.Default) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                log.info("masuk setelah upload : " + stepper.getModelObject());
                if (line + stepper.getModelObject() >= 0 && line + stepper.getModelObject() <= eq.getMaxRow() - 1)
                    line = line + stepper.getModelObject();
                target.add( chartrgb, inputan, markupLabel);
            }
        });


        add(form);
    }

    @Override
    public IModel<String> getTitleModel() {
        return new Model<>("Analisis Baris");
    }

    @Override
    public IModel<String> getMetaDescriptionModel() {
        return new Model<>("Analisis Baris");
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

    public byte[] getInput() {
        return eq.getResult();
    }
}
