/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
import Foundation


open class ImageDisplayScreenlet: FileDisplayScreenlet {


	//MARK: Inspectables

	@IBInspectable open var placeholder: UIImage?  {
		didSet {
			imageDisplayViewModel?.placeholder = placeholder
		}
	}

	open var imageDisplayViewModel: ImageDisplayViewModel? {
		return screenletView as? ImageDisplayViewModel
	}

	open var imageMode: UIViewContentMode = .scaleAspectFit {
		didSet {
			imageDisplayViewModel?.imageMode = imageMode
		}
	}

	open var placeholderImageMode: UIViewContentMode = .center {
		didSet {
			imageDisplayViewModel?.placeholderImageMode = placeholderImageMode
		}
	}


	//MARK: FileDisplayScreenlet

	override open class var supportedMimeTypes: [String] {
		return ["image/png", "image/jpg", "image/jpeg", "image/gif"]
	}


	//MARK: BaseScreenlet

	override open func onCreated() {
		super.onCreated()
		
		imageDisplayViewModel?.imageMode = imageMode
		imageDisplayViewModel?.placeholder = placeholder
		imageDisplayViewModel?.placeholderImageMode = placeholderImageMode
	}

}
