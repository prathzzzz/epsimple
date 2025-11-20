import { cn } from '@/lib/utils'

interface LogoProps extends React.SVGProps<SVGSVGElement> {
  className?: string
  variant?: 'auto' | 'dark' | 'light'
}

export function Logo({ className, variant = 'auto', ...props }: LogoProps) {
  const fillClass = variant === 'dark' 
    ? '[--logo-fill:#111038]'
    : variant === 'light'
    ? '[--logo-fill:url(#logo-gradient)]'
    : '[--logo-fill:#111038] dark:[--logo-fill:url(#logo-gradient)]'

  return (
    <div className={cn('flex items-center justify-center', fillClass, className)}>
      <svg
        width='230'
        height='60'
        viewBox='0 0 230 60'
        fill='none'
        xmlns='http://www.w3.org/2000/svg'
        className='h-full w-auto'
        {...props}
      >
        <defs>
          <linearGradient id="logo-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#FFFFFF" />
            <stop offset="50%" stopColor="#F5F5F5" />
            <stop offset="100%" stopColor="#E5E5E5" />
          </linearGradient>
        </defs>
        <text
          x='20'
          y='42'
          fontFamily="'Century Gothic', Futura, sans-serif"
          fontWeight='bold'
          fontSize='44'
          fill='var(--logo-fill)'
          letterSpacing='-1'
        >
          eps
        </text>
        <g transform='translate(114, 27)'>
          <circle
            cx='0'
            cy='0'
            r='15'
            stroke='#FF1F48'
            strokeWidth='5'
          />
          <path
            d='M4 -24 L4 5 L8 5 L8 8 L-8 8 L-8 5 L-4 5 L-4 -19 L-10 -15 L-10 -19 L-4 -24 Z'
            fill='var(--logo-fill)'
          />
        </g>
        <text
          x='132'
          y='42'
          fontFamily="'Century Gothic', Futura, sans-serif"
          fontWeight='bold'
          fontSize='44'
          fill='var(--logo-fill)'
          letterSpacing='-1'
        >
          ne
        </text>
      </svg>
    </div>
  )
}
