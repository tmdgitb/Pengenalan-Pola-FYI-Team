package com.ocr.thinning.web;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class HomePage extends PubLayout {

    public HomePage(PageParameters parameters) {
        super(parameters);
        Image res  = new Image("opening", new DynamicImageResource("image/png") {
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return getInput();
            }
        });
        add(res);
    }

    @Override
    public IModel<String> getTitleModel() {
        return new Model<>("Tugas Pattern Recognition");
    }

    @Override
    public IModel<String> getMetaDescriptionModel() {
        return new Model<>("Tugas Pattern Recognition");
    }

    public byte[] getInput(){
        Mat image = Highgui.imread("FirstPage.jpg");
        MatOfByte result = new MatOfByte();
        Highgui.imencode(".png", image, result);
        byte[] nil = result.toArray();
        return nil;
    }
}
