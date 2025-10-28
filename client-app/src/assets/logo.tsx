import { type ImgHTMLAttributes } from 'react'
import { cn } from '@/lib/utils'

export function Logo({ className, ...props }: ImgHTMLAttributes<HTMLImageElement>) {
  return (
    <img
      src='/images/eps_logo_dark.png'
      alt='EPSimple logo'
      className={cn('h-auto w-50 dark:bg-white dark:p-2 dark:rounded', className)}
      {...props}
    />
  )
}
