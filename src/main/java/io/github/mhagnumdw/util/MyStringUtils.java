package io.github.mhagnumdw.util;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

public class MyStringUtils {

    public static void append(StringBuilder sb, String value) {
        sb.append(value);
    }

    public static void newLine(StringBuilder sb, String value) {
        sb.append(value);
        newLine(sb);
    }

    /**
     * Adiciona uma nova linha ao {@code sb} formatando uma String trocando os {} pelas respectiva posição no varargs.
     *
     * @param sb
     *            StringBuilder
     * @param format
     *            formato
     * @param arguments
     *            valores dos {}
     */
    public static void newLine(StringBuilder sb, String format, Object... arguments) {
        sb.append(MyStringUtils.format(format, arguments));
        newLine(sb);
    }

    public static void newLine(StringBuilder sb1, StringBuilder sb2) {
        sb1.append(sb2);
        newLine(sb1);
    }

    /**
     * Adiciona uma nova linha para cada objeto da coleção usando o {@code toString()} do objeto.
     */
    public static void newLine(StringBuilder sb, Collection values) {
        for (Object object : values) {
            newLine(sb, object.toString());
        }
    }

    public static void newLine(StringBuilder sb) {
        sb.append("\n");
    }

    /**
     * Concatena {@code value} ao {@code sb} inserindo um espaço no meio e dando um {@code trim} no {@code value}.
     *
     * <p>
     * A ideia desse método é para montar frases, evitando espaços demasiados entre as palavras e evitando a preocupação de adicionar espaços antes e depois das concatenações.
     * </p>
     *
     * @param sb
     *            fonte
     * @param value
     *            valor a ser concatenado com {@code sb}
     */
    public static void appendWords(StringBuilder sb, String value) {
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append(value.trim());
    }

    /**
     * Replace all para {@link StringBuilder}.
     */
    public static void replaceAll(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
    }

    public static void replaceLast(StringBuilder builder, String from, String to) {
        int index = builder.lastIndexOf(from);
        builder.replace(index, index + from.length(), to);
    }

    public static String replaceLineSeparator(String str, String replacement) {
        return str.replaceAll("[\\n\\r]+", replacement);
    }

    /**
     * Trunca a String {@code str} se o tamanho for superior a {@code maxLength}. Se a String {@code str} for truncada é adicionado ao final o {@code overlay}.
     *
     * @param str
     *            a String para truncar e adicionar o overlay, se for o caso. Pode ser nulo.
     * @param maxLength
     *            tamanho em que {@code str} deve ser truncado
     * @param overlay
     *            valor que deve ser adicionado ao final do {@code str} se o {@code str} for truncado
     *
     * @return String ajustada, ou nulo se {@code str} for nulo
     */
    public static String ifTruncThenOverlay(String str, int maxLength, String overlay) {
        if (str == null) {
            return null;
        }
        if (str.length() > maxLength) {
            str = str.substring(0, maxLength - overlay.length()) + overlay;
        }
        return str;
    }

    /**
     * Retorna o menor nome possível, sempre mantendo o primeiro nome, desde que não se repita dentro da coleção.
     *
     * <p>
     * Exemplo:
     * </p>
     * <p>
     * Para a coleção: "Doug", "Valter Fi", "Dan Barros", "Dan Vasc", "Dan Cortez", "Dan Costa"
     * </p>
     *
     * <pre>
     * {@code name} = "Doug"        {@code return} "Doug"
     * {@code name} = "Valter Fi"   {@code return} "Valter"
     * {@code name} = "Dan Barros"  {@code return} "Dan B"
     * {@code name} = "Dan Vasc"    {@code return} "Dan V"
     * {@code name} = "Dan Cortez"  {@code return} "Dan Cor"
     * {@code name} = "Dan Costa"   {@code return} "Dan Cos"
     * </pre>
     *
     * @param name
     *            nome dentro da coleção
     * @param names
     *            coleção de nomes
     */
    public static String getUniqueShortName(String name, Collection<String> names) {
        int startIndex = name.indexOf(' ');
        if (startIndex < 0) {
            return name; // não precisa de trim
        }
        int endIndex = name.length() - 1;
        String prefix = name;
        for (int index = startIndex; index < endIndex; index++) {
            prefix = name.substring(0, index);
            if (existsMoreThanOneStartingWith(prefix, names)) {
                continue;
            } else {
                break;
            }
        }
        return prefix.trim();
    }

    private static boolean existsMoreThanOneStartingWith(String prefix, Collection<String> names) {
        int qtd = 0;
        for (String value : names) {
            if (value.toLowerCase().startsWith(prefix.toLowerCase()) && ++qtd > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Formata uma String trocando os {} pelas respectiva posição no varargs.
     *
     * <pre>
     * Ex:
     * System.out.println(format("Eu tenho {} anos e moro em {}.", 5, "Fortaleza"));
     * Output: Eu tenho 5 anos e moro em Fortaleza.
     * </pre>
     *
     * @param format
     *            formato
     * @param arguments
     *            valores dos {}
     *
     * @return String formatada
     */
    public static String format(String format, Object... arguments) {
        return String.format(format.replace("{}", "%s"), arguments);
    }

    /**
     * Normaliza uma String.
     *
     * <pre>
     * - converte para letras minúsculas
     * - caracteres acentuados são trocados por sua versão não acentuada
     * - remove os espaços
     * - remove as pontuações
     * </pre>
     *
     * @param str
     *            String
     *
     * @return string normalizada, ou {@code null} se {@code str} for {@code null}
     */
    public static String normalize(String str) {
        if (str == null) {
            return null;
        }
        str = str.toLowerCase();
        str = StringUtils.deleteWhitespace(str);
        str = StringUtils.stripAccents(str);
        // Talvez uma solução ainda mais simples e abrangente seja usar o regex '[^\p{Alnum}]'
        // para remover tudo que não é nem letra e nem número
        str = str.replaceAll("[\\u2000-\\u206F\\u2E00-\\u2E7F\\\\'!\"#$%&()*+,\\-.\\/:;<=>?@\\[\\]^_`{|}~]", "");
        return str;
    }

    /**
     * Constrói uma nova String decodificando o array de bytes usando o conjunto de caracteres {@link StandardCharsets#UTF_8}.
     *
     * @param data
     *            os bytes a serem decodificados em caracteres
     *
     * @return string
     */
    public static String fromUTF8(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * Alternativa ao método {@link StringUtils#firstNonBlank} que possibilita carregar os valores de modo lazy.
     *
     * @param values
     *            possíveis valores em ordem de prioridade
     *
     * @return o primeiro valor preenchido, ou {@code null} se nenhum valor é preenchido
     *
     * @see <a href="https://dzone.com/articles/leveraging-lambda-expressions-for-lazy-evaluation">https://dzone.com/articles/leveraging-lambda-expressions-for-lazy-evaluation</a>
     */
    @SafeVarargs
    public static <T extends CharSequence, S extends Supplier<T>> T firstNonBlankLazy(final S... values) {
        if (values != null) {
            for (final S valSupplier : values) {
                T val = valSupplier.get();
                if (StringUtils.isNotBlank(val)) {
                    return val;
                }
            }
        }
        return null;
    }

    /**
     * Mascara uma String se o atributo {@code mask} for {@code true}, caso contrário retorna a própria String de entrada.
     *
     * @param value
     *            String
     * @param mask
     *            {@code true} se a String deve ser mascara, {@code false} se não
     *
     * @return String de entrada, mascarada ou não
     */
    public static String mask(String value, boolean mask) {
        return mask ? "***" : value;
    }

}
