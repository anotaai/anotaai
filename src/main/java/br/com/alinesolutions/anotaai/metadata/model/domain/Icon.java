package br.com.alinesolutions.anotaai.metadata.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Icon {

	GLYPHICON_ASTERISK("Asterisk", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PLUS("Plus", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EURO("Euro", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EUR("Eur", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MINUS("Minus", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CLOUD("Cloud", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ENVELOPE("Envelope", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PENCIL("Pencil", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_GLASS("Glass", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MUSIC("Music", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SEARCH("Search", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HEART("Heart", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_STAR("Star", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_STAR_EMPTY("Star Empty", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_USER("User", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FILM("Film", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TH_LARGE("TH Large", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TH("Th", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TH_LIST("TH List", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OK("Ok", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_REMOVE("Remove", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ZOOM_IN("In", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ZOOM_OUT("Out", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OFF("Off", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SIGNAL("Signal", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_COG("Cog", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TRASH("Trash", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HOME("Home", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FILE("Film", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TIME("Time", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ROAD("Toad", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_DOWNLOAD_ALT("Download Alt", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_DOWNLOAD("Download", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_UPLOAD("Upload", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_INBOX("Inbox", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PLAY_CIRCLE("Circle", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_REPEAT("Repeat", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_REFRESH("Refresh", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LIST_ALT("List Alt", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LOCK("Lock", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FLAG("Flag", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HEADPHONES("Headphones", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_VOLUME_OFF("Volume Off", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_VOLUME_DOWN("Volume Dow", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_VOLUME_UP("Volume UP", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_QRCODE("QRCode", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BARCODE("Barcode", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TAG("Tag", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TAGS("Tags", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BOOK("Book", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BOOKMARK("Bookmark", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PRINT("Print", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CAMERA("Camera", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FONT("Font", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ITALIC("Italic", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TEXT_HEIGHT("Text Height", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TEXT_WIDTH("Text Eidth", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ALIGN_LEFT("Align Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ALIGN_CENTER("Align Center", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ALIGN_RIGHT("Align Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ALIGN_JUSTIFY("Align Justify", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LIST("List", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_INDENT_LEFT("Indent Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_INDENT_RIGHT("Indent Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FACETIME_VIDEO("Facetime Video", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PICTURE("Picture", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MAP_MARKER("Map Marker", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ADJUST("Adjust", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TINT("Tint", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EDIT("Edit", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SHARE("Share", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CHECK("Check", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MOVE("Move", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_STEP_BACKWARD("Step Backward", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FAST_BACKWARD("Fast Backward", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BACKWARD("Backward", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PLAY("Play", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PAUSE("Pause", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_STOP("Stop", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FORWARD("Forward", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FAST_FORWARD("Fast Forward", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_STEP_FORWARD("Step Forward", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EJECT("Eject", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CHEVRON_LEFT("Chevron Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CHEVRON_RIGHT("Chevron Right Sign Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PLUS_SIGN("Plus Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MINUS_SIGN("Minus Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_REMOVE_SIGN("Remove Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OK_SIGN("Ok Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_QUESTION_SIGN("Question Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_INFO_SIGN("Info Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SCREENSHOT("Screenshot", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_REMOVE_CIRCLE("Remove Circle", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OK_CIRCLE("Ok Circle", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BAN_CIRCLE("Ban Circle", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ARROW_LEFT("Arrow Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ARROW_RIGHT("Arrow Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ARROW_UP("Arrow Up", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ARROW_DOWN("Arrow Down", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SHARE_ALT("Share Alt", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RESIZE_FULL("Resize Full", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RESIZE_SMALL("Resize Small", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EXCLAMATION_SIGN("Exclamation Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_GIFT("Gift", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LEAF("Leaf", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FIRE("Fire", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EYE_OPEN("Eye Open", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EYE_CLOSE("Eye Close", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_WARNING_SIGN("Warning Sign", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PLANE("Plane", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CALENDAR("Calendar", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RANDOM("Random", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_COMMENT("Comment", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MAGNET("Magnet", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CHEVRON_UP("Up", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CHEVRON_DOWN("Down", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RETWEET("Retweet", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SHOPPING_CART("Shopping Cart", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FOLDER_CLOSE("Folder Close", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FOLDER_OPEN("Folder Open", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RESIZE_VERTICAL("Resize Vertical", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RESIZE_HORIZONTAL("Resize Horizontal", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HDD("HDD", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BULLHORN("Bullhorn", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BELL("Bell", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CERTIFICATE("Certificate", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_THUMBS_UP("Thumbs Up", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_THUMBS_DOWN("Thumbs Down", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HAND_RIGHT("Hand Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HAND_LEFT("Hand Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HAND_UP("Hand Up", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HAND_DOWN("Hand Down", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CIRCLE_ARROW_RIGHT("Circle Arrow Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CIRCLE_ARROW_LEFT("Circle Arrow Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CIRCLE_ARROW_UP("Circle Arrow Up", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CIRCLE_ARROW_DOWN("Circle Arrow Down", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_GLOBE("Globe", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_WRENCH("Wrench", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TASKS("Tasks", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FILTER("Filter", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BRIEFCASE("Briefcase", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FULLSCREEN("Fullscreen", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PAPERCLIP("Paperclip", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HEART_EMPTY("Heart Empty", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LINK("Link", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PHONE("Phone", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PUSHPIN("", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_USD("USD", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_GBP("GBP", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SORT("SORT", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SORT_BY_ALPHABET("Sort By Alphabet", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SORT_BY_ALPHABET_ALT("Sort By Alphabet Alt", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SORT_BY_ORDER_ALT("Sort By Order Alt", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SORT_BY_ATTRIBUTES("Sort By Attibutes", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SORT_BY_ATTRIBUTES_ALT("Sort By Attributes Alt", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_UNCHECKED("Unchecked", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EXPAND("Expand", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_COLLAPSE_DOWN("Collapse Down", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_COLLAPSE_UP("Collapse Up", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LOG_IN("Log In", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FLASH("Flash", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LOG_OUT("Log Out", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_NEW_WINDOW("New Window", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RECORD("Record", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SAVE("Save", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OPEN("Open", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SAVED("Saved", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_IMPORT("Import", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EXPORT("Export", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SEND("Send", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FLOPPY_DISK("Floppy Disk", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FLOPPY_SAVED("Floppy Saved", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FLOPPY_REMOVE("Floppy Remove", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FLOPPY_SAVE("Floppy Save", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_FLOPPY_OPEN("Floppu Oppen", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CREDIT_CARD("Credit Card", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TRANSFER("Transfer", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CUTLERY("Cutlery", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HEADER("Header", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_COMPRESSED("Compressed", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EARPHONE("Earphone", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PHONE_ALT("Phone Alt", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TOWER("Tower", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_STATS("Stats", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SD_VIDEO("SD Video", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HD_VIDEO("HD Video", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SUBTITLES("Subtitles", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SOUND_STEREO("Sound Stereo", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SOUND_DOLBY("Sound Dolby", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SOUND_5_1("Sound 5 1", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SOUND_6_1("Sound 6 1", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SOUND_7_1("Sound 7 1", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_COPYRIGHT_MARK("Copyrigh Mark", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_REGISTRATION_MARK("Registration Mark", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CLOUD_DOWNLOAD("Cloud Download", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CLOUD_UPLOAD("Cloud Upload", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TREE_CONIFER("Tree Conifer", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TREE_DECIDUOUS("Tree Deciduous", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CD("CD", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SAVE_FILE("Save File", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OPEN_FILE("Open File", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LEVEL_UP("Level Up", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_COPY("Copy", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PASTE("Paste", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ALERT("Alert", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EQUALIZER("Equalizer", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_KING("King", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_QUEEN("Queen", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PAWN("Pawn", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BISHOP("Bishop", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_KNIGHT("Knight", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BABY_FORMULA("Baby Formula", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TENT("Tent", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BLACKBOARD("Blackboard", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BED("Bed", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_APPLE("Apple", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ERASE("Erase", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_HOURGLASS("Hourglass", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_LAMP("", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_DUPLICATE("Duplicate", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_PIGGY_BANK("Piggy Bank", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SCISSORS("Scissors", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BITCOIN("Bitcoin", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_BTC("Btc", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_XBT("XBT", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_YEN("Yen", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_JPY("Jpy", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RUBLE("Rule", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_RUB("Rub", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SCALE("Scale", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ICE_LOLLY("Ice Lolly", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_ICE_LOLLY_TASTED("Ice Lollu Tasted", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_EDUCATION("Education", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OPTION_HORIZONTAL("Option Horizontal", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OPTION_VERTICAL("Option Vertical", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MENU_HAMBURGER("Menu Hamburger", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MODAL_WINDOW("Modal Window", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OIL("Oil", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_GRAIN("Grain", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SUNGLASSES("Sunglasses", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TEXT_SIZE("Text Size", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TEXT_COLOR("Text Collor", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TEXT_BACKGROUND("Text Background", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OBJECT_ALIGN_TOP("Object Align Top", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OBJECT_ALIGN_BOTTOM("Object Align Botton", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OBJECT_ALIGN_HORIZONTAL("Object Align Horizontal", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OBJECT_ALIGN_LEFT("Object Align Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OBJECT_ALIGN_VERTICAL("Object Vertical", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_OBJECT_ALIGN_RIGHT("Object Align Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TRIANGLE_RIGHT("Triangle Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TRIANGLE_LEFT("Triangle Left", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TRIANGLE_BOTTOM("Triangle Botton", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_TRIANGLE_TOP("Triangle Top", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_CONSOLE("Console", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SUPERSCRIPT("Supersript", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_SUBSCRIPT("Subscript", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MENU_LEFT("Menu Feft", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MENU_RIGHT("Menu Right", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MENU_DOWN("Menu Down", CategoriaIcone.GLYPHCONS, null),
	GLYPHICON_MENU_UP("Menu Up", CategoriaIcone.GLYPHCONS, null),
	PENCIL("Mode Edition", CategoriaIcone.MATERIAL, "mode_edit"),
	USER("Perm Identity", CategoriaIcone.MATERIAL, "perm_identity"),
	FILE("Payment", CategoriaIcone.MATERIAL, "payment"),
	BARCODE("Shopping cart", CategoriaIcone.MATERIAL, "shopping_cart"),
	BOOK("Library Books", CategoriaIcone.MATERIAL, "library_books"),
	TH_LIST("Business Center", CategoriaIcone.MATERIAL, "business_center"),
	TH("Applications", CategoriaIcone.MATERIAL, "apps"),
	DONE("Done", CategoriaIcone.MATERIAL, "done"),
	INFO("Information", CategoriaIcone.MATERIAL, "info"),
	WARNING("Warning", CategoriaIcone.MATERIAL, "warning"),
	ERROR("Error", CategoriaIcone.MATERIAL, "error"),
	ARROW_LEFT("List", CategoriaIcone.MATERIAL, "list");
	
	private Icon(String descricao, CategoriaIcone categoria, String className) {
		this.descricao = descricao;
		this.categoria = categoria;
		this.className =  this.className.startsWith("GLYPHICON_") ? this.toString().toLowerCase().replace("_", "-") : className;
	}
	
	private String className;
	
	private CategoriaIcone categoria;
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	public CategoriaIcone getCategoriaIcone() {
		return categoria;
	}
	
	public String getClassName() {
		return className;
	}

	// TODO - Adicionar metodos dinamicamente
	public String getType() {
		return this.toString();
	}

	public String getPropertieKey() {
		StringBuilder sb = new StringBuilder("enum.");
		sb.append(this.getClass().getName()).append(".");
		sb.append(toString());
		return sb.toString().toLowerCase();
	}

	@JsonCreator
	public static Icon fromObject(JsonNode node) {
		String type = null;
		if (node.getNodeType().equals(JsonNodeType.STRING)) {
			type = node.asText();
		} else {
			if (!node.has("type")) {
				throw new IllegalArgumentException();
			}
			type = node.get("type").asText();
		}
		return valueOf(type);
	}
	
}
