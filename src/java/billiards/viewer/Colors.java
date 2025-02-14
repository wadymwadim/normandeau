package billiards.viewer;

import javaslang.Tuple;
import javaslang.collection.HashMap;

import javafx.scene.paint.Color;

public final class Colors {
    public static final HashMap<Color, String> colorMap = HashMap.ofEntries(
        Tuple.of(Color.ALICEBLUE, "Alice Blue"), Tuple.of(Color.ANTIQUEWHITE, "Antique White"),
        Tuple.of(Color.AQUA, "Aqua"), Tuple.of(Color.AQUAMARINE, "Aquamarine"),
        Tuple.of(Color.AZURE, "Azure"), Tuple.of(Color.BEIGE, "Beige"),
        Tuple.of(Color.BISQUE, "Bisque"), Tuple.of(Color.BLACK, "Black"),
        Tuple.of(Color.BLANCHEDALMOND, "Blanched Almond"), Tuple.of(Color.BLUE, "Blue"),
        Tuple.of(Color.BLUEVIOLET, "Blue Violet"), Tuple.of(Color.BROWN, "Brown"),
        Tuple.of(Color.BURLYWOOD, "Burly Wood"), Tuple.of(Color.CADETBLUE, "Cadet Blue"),
        Tuple.of(Color.CHARTREUSE, "Chartreuse"), Tuple.of(Color.CHOCOLATE, "Chocolate"),
        Tuple.of(Color.CORAL, "Coral"), Tuple.of(Color.CORNFLOWERBLUE, "Cornflower Blue"),
        Tuple.of(Color.CORNSILK, "Cornsilk"), Tuple.of(Color.CRIMSON, "Crimson"),
        Tuple.of(Color.CYAN, "Cyan"), Tuple.of(Color.DARKBLUE, "Dark Blue"),
        Tuple.of(Color.DARKCYAN, "Dark Cyan"), Tuple.of(Color.DARKGOLDENROD, "Dark Goldenrod"),
        Tuple.of(Color.DARKGRAY, "Dark Gray"), Tuple.of(Color.DARKGREEN, "Dark Green"),
        Tuple.of(Color.DARKGREY, "Dark Grey"), Tuple.of(Color.DARKKHAKI, "Dark Khaki"),
        Tuple.of(Color.DARKMAGENTA, "Dark Magenta"),
        Tuple.of(Color.DARKOLIVEGREEN, "Dark Olive Green"),
        Tuple.of(Color.DARKORANGE, "Dark Orange"), Tuple.of(Color.DARKORCHID, "Dark Orchid"),
        Tuple.of(Color.DARKRED, "Dark Red"), Tuple.of(Color.DARKSALMON, "Dark Salmon"),
        Tuple.of(Color.DARKSEAGREEN, "Dark Sea Green"),
        Tuple.of(Color.DARKSLATEBLUE, "Dark Slate Blue"),
        Tuple.of(Color.DARKSLATEGRAY, "Dark Slate Gray"),
        Tuple.of(Color.DARKSLATEGREY, "Dark Slate Grey"),
        Tuple.of(Color.DARKTURQUOISE, "Dark Turquoise"),
        Tuple.of(Color.DARKVIOLET, "Dark Violet"), Tuple.of(Color.DEEPPINK, "Deep Pink"),
        Tuple.of(Color.DEEPSKYBLUE, "Deep Sky Blue"), Tuple.of(Color.DIMGRAY, "Dim Gray"),
        Tuple.of(Color.DIMGREY, "Dim Grey"), Tuple.of(Color.DODGERBLUE, "Dodger Blue"),
        Tuple.of(Color.FIREBRICK, "Firebrick"), Tuple.of(Color.FLORALWHITE, "Floral White"),
        Tuple.of(Color.FORESTGREEN, "Forest Green"), Tuple.of(Color.FUCHSIA, "Fuchsia"),
        Tuple.of(Color.GAINSBORO, "Gainsboro"), Tuple.of(Color.GHOSTWHITE, "Ghost White"),
        Tuple.of(Color.GOLD, "Gold"), Tuple.of(Color.GOLDENROD, "Goldenrod"),
        Tuple.of(Color.GRAY, "Gray"), Tuple.of(Color.GREEN, "Green"),
        Tuple.of(Color.GREENYELLOW, "Green Yellow"), Tuple.of(Color.GREY, "Grey"),
        Tuple.of(Color.HONEYDEW, "Honeydew"), Tuple.of(Color.HOTPINK, "Hot Pink"),
        Tuple.of(Color.INDIANRED, "Indian Red"), Tuple.of(Color.INDIGO, "Indigo"),
        Tuple.of(Color.IVORY, "Ivory"), Tuple.of(Color.KHAKI, "Khaki"),
        Tuple.of(Color.LAVENDER, "Lavender"), Tuple.of(Color.LAVENDERBLUSH, "Lavender Blush"),
        Tuple.of(Color.LAWNGREEN, "Lawn Green"), Tuple.of(Color.LEMONCHIFFON, "Lemon Chiffon"),
        Tuple.of(Color.LIGHTBLUE, "Light Blue"), Tuple.of(Color.LIGHTCORAL, "Light Coral"),
        Tuple.of(Color.LIGHTCYAN, "Light Cyan"),
        Tuple.of(Color.LIGHTGOLDENRODYELLOW, "Light Goldenrod Yellow"),
        Tuple.of(Color.LIGHTGRAY, "Light Gray"), Tuple.of(Color.LIGHTGREEN, "Light Green"),
        Tuple.of(Color.LIGHTGREY, "Light Grey"), Tuple.of(Color.LIGHTPINK, "Light Pink"),
        Tuple.of(Color.LIGHTSALMON, "Light Salmon"),
        Tuple.of(Color.LIGHTSEAGREEN, "Light Sea Green"),
        Tuple.of(Color.LIGHTSKYBLUE, "Light Sky Blue"),
        Tuple.of(Color.LIGHTSLATEGRAY, "Light Slate Gray"),
        Tuple.of(Color.LIGHTSLATEGREY, "Light Slate Grey"),
        Tuple.of(Color.LIGHTSTEELBLUE, "Light Steel Blue"),
        Tuple.of(Color.LIGHTYELLOW, "Light Yellow"), Tuple.of(Color.LIME, "Lime"),
        Tuple.of(Color.LIMEGREEN, "Lime Green"), Tuple.of(Color.LINEN, "Linen"),
        Tuple.of(Color.MAGENTA, "Magenta"), Tuple.of(Color.MAROON, "Maroon"),
        Tuple.of(Color.MEDIUMAQUAMARINE, "Medium Aquamarine"),
        Tuple.of(Color.MEDIUMBLUE, "Medium Blue"),
        Tuple.of(Color.MEDIUMORCHID, "Medium Orchid"),
        Tuple.of(Color.MEDIUMPURPLE, "Medium Purple"),
        Tuple.of(Color.MEDIUMSEAGREEN, "Medium Sea Green"),
        Tuple.of(Color.MEDIUMSLATEBLUE, "Medium Slate Blue"),
        Tuple.of(Color.MEDIUMSPRINGGREEN, "Medium Spring Green"),
        Tuple.of(Color.MEDIUMTURQUOISE, "Medium Turquoise"),
        Tuple.of(Color.MEDIUMVIOLETRED, "Medium Violet Red"),
        Tuple.of(Color.MIDNIGHTBLUE, "Midnight Blue"), Tuple.of(Color.MINTCREAM, "Mint Cream"),
        Tuple.of(Color.MISTYROSE, "Misty Rose"), Tuple.of(Color.MOCCASIN, "Moccasin"),
        Tuple.of(Color.NAVAJOWHITE, "Navajo White"), Tuple.of(Color.NAVY, "Navy"),
        Tuple.of(Color.OLDLACE, "Old Lace"), Tuple.of(Color.OLIVE, "Olive"),
        Tuple.of(Color.OLIVEDRAB, "Olive Drab"), Tuple.of(Color.ORANGE, "Orange"),
        Tuple.of(Color.ORANGERED, "Orange Red"), Tuple.of(Color.ORCHID, "Orchid"),
        Tuple.of(Color.PALEGOLDENROD, "Pale Goldenrod"),
        Tuple.of(Color.PALEGREEN, "Pale Green"),
        Tuple.of(Color.PALETURQUOISE, "Pale Turquoise"),
        Tuple.of(Color.PALEVIOLETRED, "Pale Violet Red"),
        Tuple.of(Color.PAPAYAWHIP, "Papaya Whip"), Tuple.of(Color.PEACHPUFF, "Peach Puff"),
        Tuple.of(Color.PERU, "Peru"), Tuple.of(Color.PINK, "Pink"),
        Tuple.of(Color.PLUM, "Plum"), Tuple.of(Color.POWDERBLUE, "Powder Blue"),
        Tuple.of(Color.PURPLE, "Purple"), Tuple.of(Color.RED, "Red"),
        Tuple.of(Color.ROSYBROWN, "Rosy Brown"), Tuple.of(Color.ROYALBLUE, "Royal Blue"),
        Tuple.of(Color.SADDLEBROWN, "Saddle Brown"), Tuple.of(Color.SALMON, "Salmon"),
        Tuple.of(Color.SANDYBROWN, "Sandy Brown"), Tuple.of(Color.SEAGREEN, "Sea Green"),
        Tuple.of(Color.SEASHELL, "Sea Shell"), Tuple.of(Color.SIENNA, "Sienna"),
        Tuple.of(Color.SILVER, "Silver"), Tuple.of(Color.SKYBLUE, "Sky Blue"),
        Tuple.of(Color.SLATEBLUE, "Slate Blue"), Tuple.of(Color.SLATEGRAY, "Slate Gray"),
        Tuple.of(Color.SLATEGREY, "Slate Grey"), Tuple.of(Color.SNOW, "Snow"),
        Tuple.of(Color.SPRINGGREEN, "Spring Green"), Tuple.of(Color.STEELBLUE, "Steel Blue"),
        Tuple.of(Color.TAN, "Tan"), Tuple.of(Color.TEAL, "Teal"),
        Tuple.of(Color.THISTLE, "Thistle"), Tuple.of(Color.TOMATO, "Tomato"),
        Tuple.of(Color.TRANSPARENT, "Transparent"), Tuple.of(Color.TURQUOISE, "Turquoise"),
        Tuple.of(Color.VIOLET, "Violet"), Tuple.of(Color.WHEAT, "Wheat"),
        Tuple.of(Color.WHITE, "White"), Tuple.of(Color.WHITESMOKE, "White Smoke"),
        Tuple.of(Color.YELLOW, "Yellow"), Tuple.of(Color.YELLOWGREEN, "Yellow Green"));
}
