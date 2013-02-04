/*
 * Copyright 2007-2010 JadaSite.

 * This file is part of JadaSite.
 
 * JadaSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JadaSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JadaSite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jada.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageScaler {

	byte data[] = null;
	String mimeType = null;

	public ImageScaler(byte[] data, String mimeType) {
		this.data = data;
		this.mimeType = mimeType;
	}

	public void resize(int maxsize) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		
		int sourceWidth = bufferedImage.getWidth();
		int sourceHeight = bufferedImage.getHeight();
		int resultWidth = sourceWidth;
		int resultHeight = sourceHeight;
		if (maxsize < resultWidth) {
			resultWidth = maxsize;
			resultHeight = (int) ((resultWidth * sourceHeight) / sourceWidth);
		}
		if (maxsize < resultHeight) {
			resultHeight = maxsize;
			resultWidth = (int) ((resultHeight * sourceWidth) / sourceHeight);
		}
		
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		BufferedImage resizedImage = new BufferedImage(resultWidth, resultHeight, type);
		Graphics2D g = resizedImage.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g.drawImage(bufferedImage, 0, 0, resultWidth, resultHeight, null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);


		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(resizedImage, "jpg", outputStream);
		data = outputStream.toByteArray();
	}

	public byte[] getBytes() {
		return data;
	}
	
	public int getHeight() {
		return Toolkit.getDefaultToolkit().createImage(data).getHeight(null);
	}
	
	public int getWidth() {
		return Toolkit.getDefaultToolkit().createImage(data).getWidth(null);
	}
}