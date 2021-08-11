package ru.pshiblo.love.job.parser;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.springframework.stereotype.Component;
import ru.pshiblo.love.job.parser.base.AbstractLoveParser;
import ru.pshiblo.love.job.parser.exceptions.ResponseParserException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ExtramanLoveParser extends AbstractLoveParser {

    @SneakyThrows
    @Override
    protected URI getUrl() {
        return new URI("https://extraman.ru/krasivye-slova-devushke/");
    }

    @Override
    protected List<String> parse(String response) throws ResponseParserException {
        List<String> messages = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements elements__ = document.select("#post-12126 > div.article-content.clearfix > div.entry-content.clearfix");
        for (Element div : elements__) {
            for (Element element : div.children()) {
                if (element.tag() == Tag.valueOf("p") && element.is(new Evaluator.Matches(Pattern.compile("\\d+\\. .+")))) {
                    messages.add(element.text().replaceAll("\\d+\\. ", ""));
                }
            }
        }


        return messages;
    }
}
