import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import api from '../utils/api'
import { useCart } from '../context/CartContext'
import Rating from '../components/common/Rating'
import { PageLoader } from '../components/common/Spinner'
import { formatPrice, getDiscountPercent } from '../utils/helpers'

export default function ProductDetailPage() {
  const { id } = useParams()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [qty, setQty] = useState(1)
  const [selectedImage, setSelectedImage] = useState(0)
  const { addToCart } = useCart()

  useEffect(() => {
    const fetch = async () => {
      try {
        setLoading(true)
        const { data } = await api.get(`/products/${id}`)
        setProduct(data)
      } catch (err) {
        setError('Product not found')
      } finally {
        setLoading(false)
      }
    }
    fetch()
  }, [id])

  if (loading) return <PageLoader />
  if (error) return (
    <div className="max-w-xl mx-auto text-center py-20">
      <p className="text-red-500 text-lg font-semibold">{error}</p>
      <Link to="/products" className="btn-primary mt-4 inline-block">Back to Products</Link>
    </div>
  )
  if (!product) return null

  const discount = getDiscountPercent(product.originalPrice, product.price)
  const images = product.images?.length ? product.images : [product.image]

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Breadcrumb */}
      <nav className="flex items-center gap-2 text-sm text-gray-500 mb-6">
        <Link to="/" className="hover:text-blue-600">Home</Link>
        <span>/</span>
        <Link to="/products" className="hover:text-blue-600">Products</Link>
        <span>/</span>
        <Link to={`/products?category=${product.category}`} className="hover:text-blue-600">{product.category}</Link>
        <span>/</span>
        <span className="text-gray-800 font-medium truncate max-w-xs">{product.name}</span>
      </nav>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
        {/* Images */}
        <div>
          <div className="bg-gray-50 rounded-2xl overflow-hidden mb-3 aspect-square flex items-center justify-center">
            <img
              src={images[selectedImage]}
              alt={product.name}
              className="w-full h-full object-cover"
              onError={(e) => { e.target.src = 'https://via.placeholder.com/600?text=Product' }}
            />
          </div>
          {images.length > 1 && (
            <div className="flex gap-2">
              {images.map((img, i) => (
                <button
                  key={i}
                  onClick={() => setSelectedImage(i)}
                  className={`w-16 h-16 rounded-lg overflow-hidden border-2 transition-colors ${i === selectedImage ? 'border-blue-500' : 'border-gray-200'}`}
                >
                  <img src={img} alt="" className="w-full h-full object-cover" />
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Info */}
        <div>
          <p className="text-blue-600 font-semibold uppercase tracking-wide text-sm mb-1">{product.brand}</p>
          <h1 className="text-3xl font-bold text-gray-900 mb-3">{product.name}</h1>

          <div className="flex items-center gap-3 mb-4">
            <Rating value={product.rating} numReviews={product.numReviews} size="md" />
            <span className="text-sm text-gray-500">{product.numReviews} reviews</span>
          </div>

          {/* Price */}
          <div className="flex items-center gap-3 mb-4">
            <span className="text-3xl font-extrabold text-gray-900">{formatPrice(product.price)}</span>
            {discount > 0 && (
              <>
                <span className="text-xl text-gray-400 line-through">{formatPrice(product.originalPrice)}</span>
                <span className="badge bg-red-100 text-red-600 text-sm">Save {discount}%</span>
              </>
            )}
          </div>

          {/* Stock status */}
          <div className="flex items-center gap-2 mb-5">
            <div className={`w-2.5 h-2.5 rounded-full ${product.countInStock > 0 ? 'bg-green-500' : 'bg-red-400'}`} />
            <span className={`text-sm font-medium ${product.countInStock > 0 ? 'text-green-700' : 'text-red-600'}`}>
              {product.countInStock > 0 ? `In Stock (${product.countInStock} available)` : 'Out of Stock'}
            </span>
          </div>

          <p className="text-gray-600 leading-relaxed mb-6">{product.description}</p>

          {/* Tags */}
          {product.tags?.length > 0 && (
            <div className="flex flex-wrap gap-2 mb-6">
              {product.tags.map((tag) => (
                <span key={tag} className="badge bg-gray-100 text-gray-600">{tag}</span>
              ))}
            </div>
          )}

          {/* Quantity + Add to Cart */}
          {product.countInStock > 0 && (
            <div className="flex items-center gap-3 mb-4">
              <div className="flex items-center border border-gray-200 rounded-xl overflow-hidden">
                <button
                  onClick={() => setQty(q => Math.max(1, q - 1))}
                  className="w-10 h-10 flex items-center justify-center text-gray-600 hover:bg-gray-100 transition-colors text-lg"
                >−</button>
                <span className="w-12 text-center font-semibold">{qty}</span>
                <button
                  onClick={() => setQty(q => Math.min(product.countInStock, q + 1))}
                  className="w-10 h-10 flex items-center justify-center text-gray-600 hover:bg-gray-100 transition-colors text-lg"
                >+</button>
              </div>
              <button
                onClick={() => addToCart(product._id, qty)}
                className="btn-primary flex-1 flex items-center justify-center gap-2"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4m1.6 8a2 2 0 100 4 2 2 0 000-4zm10 0a2 2 0 100 4 2 2 0 000-4z" />
                </svg>
                Add to Cart
              </button>
            </div>
          )}

          <div className="bg-blue-50 rounded-xl p-4 text-sm text-blue-800 space-y-1">
            <p>✓ Free shipping on orders over $50</p>
            <p>✓ 30-day hassle-free returns</p>
            <p>✓ Secure checkout</p>
          </div>
        </div>
      </div>
    </div>
  )
}
