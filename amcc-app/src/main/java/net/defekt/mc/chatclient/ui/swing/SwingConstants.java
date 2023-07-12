package net.defekt.mc.chatclient.ui.swing;

/**
 * Internal constant UI values
 *
 * @author Defective4
 */
public class SwingConstants {
    protected static final char[] minecraftiaChars = " \r\n!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿŀŁłŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠšŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſƂƃƏƒƵƶƷǤǥǦǧǨǩǪǫǮǯǺǻǼǽǾǿȘșȚțȷəʒˆˇˉˊˋ˘˙˚˛˜˝ʹ͵;΄΅Ά·ΈΉΊΌΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώϗЀЁЂЃЄЅІЇЈЉЊЋЌЍЎЏАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяѐёђѓєѕіїјљњћќѝўџѢѣѲѳѴѵҊҋҌҍҎҏҐґҒғҖҗҘҙҚқҜҝҠҡҢңҪҫҮүҰұҲҳҶҷҸҹҺһӀӁӂӅӆӇӈӉӊӍӎӏӒӓӔӕӘәӢӣӤӥӦӧӨөӬӭӮӯӰӱẀẁẂẃẄẅẞỲỳ‘’‚‛“”„‟†‡•‣…‰′″‵‶‹›⁄⁰ⁱ⁴⁵⁶⁷⁸⁹₀₁₂₃₄₅₆₇₈₉₩€₮₱₴₸₹₺℅ℓ№℗℠™℮⅐⅑⅒⅓⅔⅕⅖⅗⅘⅙⅚⅛⅜⅝⅞←↑→↓↔↕↖↗↘↙∅∆√∞∫∴≈≠≤≥■□▲△▶▷○●♀♂♩♪♫♭♮♯⟨⟩ﬁﬂ".toCharArray();

    protected static boolean checkMCSupported(final String s) {
        for (final char c : s.toCharArray())
            if (!containsChar(c)) return false;
        return true;
    }

    private static boolean containsChar(final char c) {
        for (int x = 0; x < minecraftiaChars.length; x++)
            if (minecraftiaChars[x] == c) return true;
        return false;
    }
}
