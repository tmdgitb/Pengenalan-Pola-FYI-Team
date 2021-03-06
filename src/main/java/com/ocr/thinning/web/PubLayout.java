package com.ocr.thinning.web;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.MetaTag;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

public abstract class PubLayout extends WebPage {

    protected NotificationPanel notificationPanel;

    public abstract IModel<String> getTitleModel();

    public abstract IModel<String> getMetaDescriptionModel();

    @Inject
    protected Environment env;

    public PubLayout(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //add(new GrowlBehavior());
        add(new Label("title", getTitleModel()));
        add(new MetaTag("metaDescription", new Model<>("description"), getMetaDescriptionModel()));
        add(new BookmarkablePageLink<>("equalizationLink",HistogramServPage.class));
        add(new BookmarkablePageLink<>("chainCodeLink",ChainCodePage.class));
        add(new BookmarkablePageLink<>("zhangSuenLink",ZhangSuenThinning.class));
        add(new BookmarkablePageLink<>("otsuOnlyLink",OtsuOnlyPage.class));
        add(new BookmarkablePageLink<>("otsuLink",OtsuThresholdingPage.class));
        add(new BookmarkablePageLink<>("sharpnessLink",ImageSharpnessPage.class));
        add(new BookmarkablePageLink<>("convolutionLink",ConvolutionPage.class));
        add(new BookmarkablePageLink<>("lineObserverLink",LineObserverPage.class));
        add(new BookmarkablePageLink<>("colorMappingLink",ColorMapGroupingPage.class));
        //       add(new BookmarkablePageLink<>("histogramLink", HistogramPage.class));
//        add(new BookmarkablePageLink<>("roadsLink", RoadListPage.class,
//                new PageParameters().set(SeoBookmarkableMapper.LOCALE_PREF_ID_PARAMETER, localePrefId)));
//        add(new BookmarkablePageLink<>("camerasLink", CameraListPage.class,
//                new PageParameters().set(SeoBookmarkableMapper.LOCALE_PREF_ID_PARAMETER, localePrefId)));
//
//        add(new BookmarkablePageLink<>("tweetListLink", TweetListPage.class,
//                new PageParameters().set(SeoBookmarkableMapper.LOCALE_PREF_ID_PARAMETER, localePrefId)));
//        add(new BookmarkablePageLink<>("tweetMapLink", TweetMapPage.class,
//                new PageParameters().set(SeoBookmarkableMapper.LOCALE_PREF_ID_PARAMETER, localePrefId)));
        notificationPanel = new NotificationPanel("notificationPanel");
        notificationPanel.setOutputMarkupId(true);
        add(notificationPanel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCssReference.instance()));
    }

}
