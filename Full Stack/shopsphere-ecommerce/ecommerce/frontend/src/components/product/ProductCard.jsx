import { Link } from 'react-router-dom'
import { useCart } from '../../context/CartContext'
import Rating from '../common/Rating'
import { formatPrice, getDiscountPercent } from '../../utils/helpers'

export default function ProductCard({ product }) {
  const { addToCart } = useCart()
  const discount = getDiscountPercent(product.originalPrice, product.price)

  return (
    <div className="card group overflow-hidden flex flex-col">
      <Link to={`/products/${product._id}`} className="relative overflow-hidden bg-gray-50">
        <img
          src={product.image}
          alt={product.name}
          className="w-full h-52 object-cover group-hover:scale-105 transition-transform duration-300"
          onError={(e) => { e.target.src = 'https://via.placeholder.com/400x300?text=Product' }}
        />
        {discount > 0 && (
          <span className="absolute top-2 left-2 badge bg-red-500 text-white">
            -{discount}%
          </span>
        )}
        {product.countInStock === 0 && (
          <div className="absolute inset-0 bg-black/40 flex items-center justify-center">
            <span className="bg-white text-gray-800 font-semibold px-3 py-1 rounded-full text-sm">Out of Stock</span>
          </div>
        )}
      </Link>

      <div className="p-4 flex flex-col flex-1">
        <p className="text-xs text-blue-600 font-semibold uppercase tracking-wide mb-1">{product.brand}</p>
        <Link to={`/products/${product._id}`}>
          <h3 className="font-semibold text-gray-900 leading-snug hover:text-blue-600 transition-colors line-clamp-2 mb-2">
            {product.name}
          </h3>
        </Link>

        <div className="mb-3">
          <Rating value={product.rating} numReviews={product.numReviews} />
        </div>

        <div className="flex items-center gap-2 mb-4">
          <span className="text-lg font-bold text-gray-900">{formatPrice(product.price)}</span>
          {discount > 0 && (
            <span className="text-sm text-gray-400 line-through">{formatPrice(product.originalPrice)}</span>
          )}
        </div>

        <button
          onClick={() => addToCart(product._id)}
          disabled={product.countInStock === 0}
          className="btn-primary w-full text-sm mt-auto flex items-center justify-center gap-2"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4m1.6 8a2 2 0 100 4 2 2 0 000-4zm10 0a2 2 0 100 4 2 2 0 000-4z" />
          </svg>
          {product.countInStock === 0 ? 'Out of Stock' : 'Add to Cart'}
        </button>
      </div>
    </div>
  )
}
