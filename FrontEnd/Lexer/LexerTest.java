package FrontEnd.Lexer;

import Enums.tokenType;
import FrontEnd.Lexer.Lexer;
import FrontEnd.Lexer.Token;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LexerTest {

    @Test
    public void testRecognizeFormatString() {
        // 创建一个Lexer实例
        Lexer lexer = new Lexer(new StringBuilder("printf(\"$$$$\");"));

        // 调用nextToken()以移动位置并识别字符串
        lexer.analysisLexer();
        ArrayList<Token> tokens = lexer.getTokens();

        // 验证返回的Token是否符合预期
        assertNull(tokens.get(1));
    }
}
